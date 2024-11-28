import json
import textwrap
import uuid
import datetime
import random
from faker import Faker

from main import find_topics_in_text

fake = Faker()


def get_users(num_users: int) -> list[dict]:
    words = fake.words(num_users, unique=True)
    return [
        {
            "account_id": str(uuid.uuid4()),
            "username": "{}{}".format(word, random.randint(0, 100)),
            "created_at": datetime.datetime.now().strftime("%Y-%m-%dT%H:%M:%S.%f"),
        }
        for word in words
    ]


def get_tags_for_post(post: str) -> list[str]:
    tags: list[str] = list()
    if bool(random.getrandbits(1)):
        topics = find_topics_in_text(post)
        # Append topics as hashtags
        for topic in topics:
            if topic["score"] > 0.5:
                tags.append("#" + topic["label"].replace(" ", "_").replace("-", "_"))
    return tags


def get_post_with_tags(post: str, account: dict) -> dict:
    tags = get_tags_for_post(post)
    post = "{} {}".format(post, " ".join(tags))

    return {
        "post_id": str(uuid.uuid4()),
        "url": "",
        "created_at": datetime.datetime.now().strftime("%Y-%m-%dT%H:%M:%S.%f"),
        "account": account,
        "content": post,
        "metrics": {
            "reposts_count": random.randint(0, 100),
            "favourites": random.randint(0, 100),
            "replies_count": random.randint(0, 100),
        },
        "hashtag": [
            {
                "name": tag,
                "url": "",
            }
            for tag in [tag.strip("#") for tag in post.split() if tag.startswith("#")]
        ],
        "media_attachments": [],
        "edited": False,
    }


def write_posts(posts: list[str]) -> None:
    accounts = get_users(round(len(posts) / 2))

    posts_s = list()
    for post in posts:
        if len(post) < 400:
            posts_s.append(get_post_with_tags(post, random.choice(accounts)))
        else:
            account = random.choice(accounts)
            parts = textwrap.wrap(post, 400)
            for part in parts:
                posts_s.append(get_post_with_tags(part, account))

    with open("data.json", "w") as f:
        json.dump(posts_s, f)
    return None
