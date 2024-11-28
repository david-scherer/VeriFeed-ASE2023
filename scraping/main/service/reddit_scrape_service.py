import json

import praw
import prawcore
from flask import Response

from main.utils.base_logger import get_logger
from .base_scrape_service import BaseScrapeService
from ..mapper.post_mapper import PostMapper
from ..models.errors.config_reader_error import ConfigReaderError
from ..models.source import Source, SourceType

HEADERS = {"Content-Type": "application/json"}

REQUIRED_REDDIT_DETAILS = ["subreddits"]

logger = get_logger("reddit_scrape_service")


class RedditScrapeService(BaseScrapeService):
    """
    Service for scraping trending statuses from reddit instances (subreddits).
    """

    def __init__(self):
        super().__init__()
        self.headers = HEADERS
        self.subreddits_as_json = self.load_social_media_details("REDDIT")
        self.reddit_api = self.set_up_reddit_api()

    def set_up_reddit_api(self) -> praw.Reddit | None:
        """
        Set up the reddit API with the credentials/details from the configuration file.
        :return: Praw Reddit API
        """

        try:
            client_id = self.config_reader.read_config("REDDIT", "CLIENT_ID")
            client_secret = self.config_reader.read_config("REDDIT", "CLIENT_SECRET")
            user_agent = self.config_reader.read_config("REDDIT", "USER_AGENT")

            return praw.Reddit(
                client_id=client_id, client_secret=client_secret, user_agent=user_agent
            )
        except ConfigReaderError as e:
            logger.error(f"Error: {e}")
            return None

    def scrape(self):
        if not self.reddit_api:
            error_message = "Reddit API could not be initialized!"
            logger.error(f"Error: {error_message}")
            return Response(status=500, response=error_message)

        if not self.subreddits_as_json:
            error_message = "No subreddits found in the configuration file!"
            logger.error(f"Error: {error_message}")
            return Response(status=404, response=error_message)

        if not self.check_required_keys_in_json_object(
            self.subreddits_as_json, REQUIRED_REDDIT_DETAILS
        ):
            error_message = (
                f"Mandatory details for reddit scraping are missing in the "
                f"configuration file! Required details: {REQUIRED_REDDIT_DETAILS}!"
                f"Please check the configuration file!"
            )
            logger.error(f"Error: {error_message}")
            return Response(status=404, response=error_message)

        try:
            top_posts_limit = int(
                self.config_reader.read_config("REDDIT", "TOP_POSTS_LIMIT")
            )
        except ConfigReaderError as e:
            logger.error(f"Error: {e}")
            top_posts_limit = 10

        subreddit_names = self.subreddits_as_json.get("subreddits", [])

        logger.info(
            f"Scraping triggered for subreddits: "
            f"{subreddit_names} with top posts limit: {top_posts_limit}"
        )

        post_collections = []

        for subreddit_name in subreddit_names:
            logger.info(f"Scraping started for subreddit '{subreddit_name}' ...")

            subreddit = self.reddit_api.subreddit(subreddit_name)

            praw_trending_statuses = subreddit.top(
                time_filter="day", limit=top_posts_limit
            )

            # Convert the praw statuses to json because
            # the praw statuses (submissions) are not serializable
            trending_statuses = [
                self.convert_praw_reddit_status_to_json(status)
                for status in praw_trending_statuses
            ]

            if not trending_statuses:
                logger.warning(
                    f"No trending statuses found for subreddit '{subreddit_name}'!"
                )
                continue

            url = f"https://www.reddit.com/r/{subreddit_name}"
            source = Source(url=url, source_type=SourceType.REDDIT)

            trending_statuses = self.add_additional_scraping_fields_to_statuses(
                statuses=trending_statuses
            )

            post_collection = self.transform_status_dtos_to_post_collection(
                source=source,
                statuses=trending_statuses,
                mapping_function=PostMapper.map_reddit_post_to_dto,
            )
            post_collections.append(post_collection)

            logger.info(
                f"Scraping finished for subreddit '{subreddit_name}'! "
                f"Number of trending statuses: {len(trending_statuses)}"
            )

        return self.send_post_collections_to_extraction_service(post_collections)

    @staticmethod
    def convert_praw_reddit_status_to_json(
        status: praw.reddit.models.Submission,
    ) -> dict:
        """
        Convert a praw reddit status to a json object.
        :param status: Praw reddit status
        :return: Json object
        """

        return {
            "id": status.id,
            "permalink": status.permalink,
            "title": status.title,
            "selftext": status.selftext,
            "link_flair_text": status.link_flair_text,
            "subreddit_name_prefixed": status.subreddit_name_prefixed,
            "subreddit": status.subreddit,
            "author": {
                "name": status.author.name,
                "id": status.author.id,
                "created_utc": status.author.created_utc,
            },
            "score": status.score,
            "num_comments": status.num_comments,
            "num_crossposts": status.num_crossposts,
            "created_utc": status.created_utc,
            "edited": status.edited,
            "media_metadata": status.media_metadata
            if hasattr(status, "media_metadata")
            else {},
            "url": status.url,
        }

    def add_additional_fields_to_status(
        self, status: dict, api_url: str = None
    ) -> dict:
        return status

    def scrape_status_with_url(self, url: str) -> Response:
        logger.info(f"Scraping reddit status with URL '{url}' triggered!")

        try:
            status = self.reddit_api.submission(url=url)
            status = self.convert_praw_reddit_status_to_json(status)

            source = Source(
                url=f"https://www.reddit.com/r/{status.get('subreddit')}",
                source_type=SourceType.REDDIT,
            )

            statuses = self.add_additional_scraping_fields_to_statuses(
                statuses=[status]
            )

            post_collection = self.transform_status_dtos_to_post_collection(
                source=source,
                statuses=statuses,
                mapping_function=PostMapper.map_reddit_post_to_dto,
            )

            logger.info(f"Scraping reddit status with URL '{url}' finished!")

            return self.send_post_collections_to_extraction_service([post_collection])
        except prawcore.exceptions.NotFound as e:
            logger.error(f"Error: {e}")
            return Response(
                status=404, response=json.dumps({"error": "Status not found!"})
            )
        except Exception as e:
            logger.error(f"Unexpected error: {e}")
            return Response(
                status=500, response=json.dumps({"error": "Scraping status failed!"})
            )
