import json_output
from random import randint, getrandbits, sample
from argparse import ArgumentParser, BooleanOptionalAction
import shutil
import os.path
import textwrap
from concurrent.futures import (
    Executor,
    ThreadPoolExecutor,
    as_completed,
)

import psycopg
from psycopg.errors import UniqueViolation
from psycopg.rows import Row, dict_row
from psycopg import Cursor
from faker import Faker
from datasets import load_dataset, concatenate_datasets
from transformers import pipeline

from mastodon import Mastodon, MastodonError

SECRETS_DIR = "./secrets"
SECRET_FILE = "initializer.secret"
USER_PW = "mastodonadmin"

fake = Faker()

DEFAULT_DB_NAME = "mastodon_development"
DEFAULT_DB_PASSWORD = ""
DEFAULT_DB_USERNAME = "verifeed"
DEFAULT_DB_HOST = "localhost"
DEFAULT_DB_PORT = 5433


def create_users(num_users: int) -> None:
    futures = list()
    print("Creating users...")
    words = fake.words(num_users, unique=True)
    with ThreadPoolExecutor(max_workers=os.cpu_count() - 1) as executor:
        for word in words:
            kw = {
                "word": word,
            }
            futures.append(executor.submit(create_user, **kw))
        for future in as_completed(futures):
            print(future.result())


def create_user(word: str) -> str:
    username = "{}{}".format(word, randint(0, 100))
    email = "{}@localhost".format(username)
    mastodon = Mastodon(client_id="{}/{}".format(SECRETS_DIR, SECRET_FILE))
    try:
        mastodon.create_account(
            username,
            USER_PW,
            email,
            agreement=True,
            to_file="{}/{}.secret".format(SECRETS_DIR, username),
        )
        return username
    except MastodonError as e:
        print("Error during creation of account {}: {}".format(username, e))


def verify_users(dbstring: str) -> None:
    with psycopg.connect(dbstring) as conn:
        with conn.cursor() as cur:
            cur.execute("UPDATE users SET confirmed_at = now()")
        conn.commit()


def get_usernames(dbstring: str) -> list[str]:
    with psycopg.connect(dbstring) as conn:
        with conn.cursor() as cur:
            cur.execute("SELECT username FROM accounts")
            return list(map(lambda row: row[0], cur.fetchall()))


def get_statuses(dbstring: str) -> list[str]:
    with psycopg.connect(dbstring) as conn:
        with conn.cursor(row_factory=dict_row) as cur:
            cur.execute("SELECT * FROM statuses")
            return cur.fetchall()


def get_status_texts_with_tags(dbstring: str) -> list[str]:
    with psycopg.connect(dbstring) as conn:
        with conn.cursor() as cur:
            cur.execute(
                "SELECT s.text FROM statuses_tags t JOIN statuses s ON s.id = t.status_id"
            )
            data = cur.fetchall()
            data = [x[0] for x in data]
            return data


def create_posts(usernames: list[str], posts: list[str]) -> list[dict]:
    print("Creating posts...")
    futures = list()
    status_list: list[dict] = list()

    lower_bound = 0
    upper_bound = randint(0, min(100, len(posts)))
    with ThreadPoolExecutor(max_workers=os.cpu_count() - 1) as executor:
        while upper_bound + 1 <= len(posts):
            username = usernames.pop()
            kw = {
                "username": username,
                "status_list": status_list,
                "posts_to_handle": posts[lower_bound:upper_bound],
            }
            futures.append(executor.submit(create_posts_as_user, **kw))
            lower_bound = upper_bound + 1
            upper_bound = randint(lower_bound, min(lower_bound + 100, len(posts)))
        for future in as_completed(futures):
            print(future.result())

    return status_list


def create_posts_as_user(
    status_list: list[dict], username: str, posts_to_handle: list[str]
) -> None:
    mastodon = Mastodon(client_id="{}/{}".format(SECRETS_DIR, SECRET_FILE))
    mastodon.log_in(
        "{}@localhost".format(username),
        USER_PW,
        to_file="{}/initializer_{}.secret".format(SECRETS_DIR, username),
    )

    print("Creating {} posts with user {}...".format(len(posts_to_handle), username))

    for post in posts_to_handle:
        post_text = post
        if len(post_text) > 400:
            print("Creating multipost as user {}".format(username))
            handle_multipost(mastodon, status_list, post_text)
        else:
            print("Creating post as user {}".format(username))
            handle_single_post(mastodon, status_list, post_text)

    return None


def handle_single_post(
    mastodon: Mastodon, status_list: list[dict], post_text: str
) -> None:
    tags: list[str] = list()
    if bool(getrandbits(1)):
        topics = find_topics_in_text(post_text)
        # Append topics as hashtags
        for topic in topics:
            if topic["score"] > 0.5:
                tags.append("#" + topic["label"].replace(" ", "_").replace("-", "_"))
    try:
        status_list.append(
            mastodon.status_post(
                "{} {}".format(post_text, " ".join(tags)), visibility="public"
            )
        )
    except MastodonError as e:
        print("Error during creation of post {}: {}".format(part[0:20], e))
    return None


def handle_multipost(
    mastodon: Mastodon, status_list: list[dict], post_text: str
) -> None:
    parts = textwrap.wrap(post_text, 400)
    most_recent_status = mastodon.status_post(parts[0], visibility="public")
    status_list.append(most_recent_status)
    for part in parts[1:]:
        tags: list[str] = list()
        if bool(getrandbits(1)):
            topics = find_topics_in_text(part)
            # Append topics as hashtags
            for topic in topics:
                if topic["score"] > 0.5:
                    tags.append(
                        "#" + topic["label"].replace(" ", "_").replace("-", "_")
                    )
        try:
            most_recent_status = mastodon.status_reply(
                most_recent_status,
                "{} {}".format(part, " ".join(tags)),
                visibility="public",
            )
            status_list.append(most_recent_status)
        except MastodonError as e:
            print("Error during creation of post {}: {}".format(part[0:20], e))
    return None


def find_topics_in_text(post_text: str):
    pipe = pipeline("text-classification", model="scroobiustrip/topic-model-v3")
    topics = pipe(post_text)
    return topics


def create_activity(usernames: list[str], status_list: list[dict]) -> None:
    print("Creating activity...")
    futures = list()
    with ThreadPoolExecutor(max_workers=os.cpu_count() - 1) as executor:
        for username in usernames:
            kw = {
                "username": username,
                "status_list": status_list,
            }
            futures.append(executor.submit(create_activity_as_user, **kw))
        for future in as_completed(futures):
            print(future.result())
    return None


def create_activity_as_user(username: str, status_list: list[dict]) -> None:
    mastodon = Mastodon(client_id="{}/{}".format(SECRETS_DIR, SECRET_FILE))
    mastodon.log_in(
        "{}@localhost".format(username),
        USER_PW,
        to_file="{}/initializer_{}.secret".format(SECRETS_DIR, username),
    )

    # # Favourite
    # statuses_to_target = sample(status_list, randint(0, round(len(status_list) / 3)))
    # for status in statuses_to_target:
    #     try:
    #         print("Favouriting status {} with user {}".format(status["id"], username))
    #         mastodon.status_favourite(status["id"])
    #     except MastodonError as e:
    #         print("Error during activity creation {}: {}".format(status, e))

    # Reblog
    statuses_to_target = sample(status_list, randint(0, len(status_list)))
    for status in statuses_to_target:
        try:
            print("Reblogging status {} with user {}".format(status["id"], username))
            mastodon.status_reblog(status["id"], visibility="public")
        except MastodonError as e:
            print("Error during activity creation {}: {}".format(status, e))

    # Comment
    statuses_to_target = sample(status_list, randint(0, len(status_list)))
    for status in statuses_to_target:
        try:
            print("Replying to status {} with user {}".format(status["id"], username))
            mastodon.status_reply(status, fake.sentence(), visibility="public")
        except MastodonError as e:
            print("Error during activity creation {}: {}".format(status, e))

    return None


if __name__ == "__main__":
    parser = ArgumentParser(
        prog="dbinitializer",
    )
    parser.add_argument("command")
    parser.add_argument("--users", required=False, default=150, type=int)
    parser.add_argument("--posts", required=False, default=500, type=int)
    parser.add_argument("--host", required=False, default=DEFAULT_DB_HOST)
    parser.add_argument("--port", required=False, default=DEFAULT_DB_PORT)
    parser.add_argument("--password", required=False, default=DEFAULT_DB_PASSWORD)
    parser.add_argument("--username", required=False, default=DEFAULT_DB_USERNAME)
    parser.add_argument("--name", required=False, default=DEFAULT_DB_NAME)
    parser.add_argument("--json", action=BooleanOptionalAction, default=False)

    args = parser.parse_args()

    if args.json is False:
        dbstring = "host={} port={} dbname={} user={}".format(
            args.host, args.port, args.name, args.username
        )
        if len(args.password) > 0:
            dbstring += " password={}".format(args.password)

        if os.path.isdir(SECRETS_DIR):
            shutil.rmtree(SECRETS_DIR)
        print("Creating client: {}/{}".format(SECRETS_DIR, SECRET_FILE))
        os.mkdir(SECRETS_DIR)
        # Initialize sdk client
        Mastodon.create_app(
            "initializer",
            api_base_url="http://localhost:3000",
            to_file="{}/{}".format(SECRETS_DIR, SECRET_FILE),
        )

    match args.command:
        case "users":
            create_users(int(args.users))
            verify_users(dbstring)
        case "posts":
            twitter_posts = load_dataset("carblacac/twitter-sentiment-analysis")
            # twitter_financial_news = load_dataset(
            #    "zeroshot/twitter-financial-news-topic"
            # )
            real_and_fake_news = load_dataset("GonzaloA/fake_news")
            posts = concatenate_datasets(
                [
                    twitter_posts["train"],
                    # twitter_financial_news["train"],
                    real_and_fake_news["train"],
                ]
            ).to_dict()["text"]

            covid_dataset: list[str] = load_dataset(
                "justinqbui/covid_fact_checked_polifact"
            )["train"].to_dict()["claim"]
            covid_dataset = [x + "#covid #vaccine" for x in covid_dataset]

            if args.json is True:
                json_output.write_posts(sample(posts + covid_dataset, 30))
            else:
                # Create posts
                usernames = get_usernames(dbstring)
                create_posts(
                    usernames, sample(posts + covid_dataset, len(posts + covid_dataset))
                )

                # Now focus on replicating already existing posts with hashtags to
                # make them trend
                existing_posts = get_status_texts_with_tags(dbstring)
                create_posts(usernames, sample(existing_posts, len(existing_posts)))
        case "activity":
            usernames = get_usernames(dbstring)
            status_list = get_statuses(dbstring)
            create_activity(usernames, status_list)
        case other:
            print("Not a valid command")
