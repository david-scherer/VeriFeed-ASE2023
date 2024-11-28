import json
import os
import sys
from abc import abstractmethod
from datetime import datetime

import validators
from flask import Response, request

from main.utils.base_logger import get_logger
from ..models.errors.config_reader_error import ConfigReaderError
from ..models.errors.http_request_error import HttpRequestError
from ..models.post_collection import PostCollection
from ..models.source import Source, SourceType
from ..utils.config_reader import ConfigReader
from ..utils.scraper_util import make_http_request, identify_social_media_platform

logger = get_logger("base_scrape_service")


class BaseScrapeService:
    """
    Base scrape class with functions for all social media scraping services.
    """

    def __init__(self):
        try:
            self.config_reader = ConfigReader()
        except FileNotFoundError as e:
            logger.error(f"Error: {e}")
            sys.exit(1)
        self.trigger_timestamp = datetime.now()

    @abstractmethod
    def scrape(self):
        """
        Scrape the social media for trending statuses.
        The specific implementation of this method is implemented in the derived classes.
        """

    def add_additional_scraping_fields_to_statuses(
        self, statuses: list, api_url: str = None
    ) -> list:
        """
        Add additional fields to the given list of statuses.
        :param statuses: List of statuses to add additional fields to
        :param api_url: API URL of the social media if available
        :return: List of statuses with additional fields
        """

        logger.debug(f"Adding additional scraping fields to {len(statuses)} statuses!")
        for status in statuses:
            status["additional_scraping_fields"] = {}
            self.add_additional_fields_to_status(status, api_url)
        return statuses

    @abstractmethod
    def add_additional_fields_to_status(
        self, status: dict, api_url: str = None
    ) -> dict:
        """
        Add additional fields to a single status.
        :param status: Status to add additional fields to
        :param api_url: API URL of the social media if available
        :return: Status with additional fields
        """

    def send_post_collections_to_extraction_service(
        self, post_collections: list
    ) -> Response:
        """
        Send the given list of post collections to the extraction service.
        :param post_collections: List of post collections to send to the extraction service
        :return: Response of the extraction service
        """

        if not post_collections:
            logger.info("No post collections to send to the extraction service!")
            return Response(status=200)

        logger.info(
            f"Sending {len(post_collections)} post collections to the extraction service!"
        )

        error_logs, info_logs = [], []
        for post_collection in post_collections:
            (
                error_log,
                info_log,
            ) = self.__send_post_collection_to_extraction_service(post_collection)
            if error_log:
                error_logs.append(error_log)
            if info_log:
                info_logs.append(info_log)

        if error_logs and info_logs:
            return Response(
                status=207,
                response=json.dumps({"error": error_logs, "info": info_logs}),
            )
        if error_logs:
            return Response(status=500, response=json.dumps({"error": error_logs}))
        return Response(status=200, response=json.dumps({"info": info_logs}))

    @abstractmethod
    def scrape_status_with_url(self, url: str) -> Response:
        """
        Scrape a single status from a given URL.
        :param url: URL to scrape status from
        :return: Response object
        """

    @staticmethod
    def scrape_single_status() -> Response:
        """
        Scrape the status of a social media platform with an url provided in the request.
        :return: Response object with error message if an error occurred, otherwise the response
        of the extraction service
        """

        data = request.get_json()
        url = data.get("url", None)

        if not url or not validators.url(url):
            return Response(
                status=400, response=json.dumps({"error": "Invalid or no url provided"})
            )
        platform = identify_social_media_platform(url)
        if platform == SourceType.UNKNOWN:
            return Response(
                status=422, response=json.dumps({"error": "unknown platform"})
            )
        if platform in [SourceType.FACEBOOK, SourceType.TWITTER]:
            return Response(
                status=422,
                response=json.dumps({"error": f"{platform.value} is not supported"}),
            )
        if platform == SourceType.MASTODON:
            from .mastodon_scrape_service import MastodonScrapeService
            return MastodonScrapeService(False).scrape_status_with_url(url)
        if platform == SourceType.REDDIT:
            from .reddit_scrape_service import RedditScrapeService
            return RedditScrapeService().scrape_status_with_url(url)

        return Response(status=422, response=json.dumps({"error": "unknown platform"}))

    def __send_post_collection_to_extraction_service(self, post_collection) -> tuple:
        """
        Is used by send_post_collections_to_extraction_service to send
        a single post collection to the extraction service.
        :param post_collection: Post collection to send to the extraction service
        :return: Tuple of error message and info message
        """

        logger.info(
            f"Sending post collection with id '{post_collection.post_collection_id}' "
            f"(Source: '{post_collection.source.source_type.value}' "
            f"| '{post_collection.source.url}') "
            f"to the extraction service!"
        )
        try:
            extraction_service_url = self.config_reader.read_config(
                "EXTRACTION-SERVICE", "EXTRACTION_SERVICE_URL"
            )
        except ConfigReaderError as e:
            error_message = f"Error: {e}"
            logger.error(error_message)
            return error_message, None
        try:
            response = make_http_request(
                headers={"Content-Type": "application/json"},
                method="POST",
                url=extraction_service_url,
                json_data=post_collection.to_dict(),
            )

            info_message = (
                f"Successfully sent post collection "
                f"with id '{post_collection.post_collection_id}' "
                f"(Source: '{post_collection.source.source_type.value}' "
                f"| '{post_collection.source.url}') "
                f"to the extraction service! "
                f"Response: {response.status_code}, {response.content.decode('utf-8')}"
            )
            logger.info(info_message)
            return None, info_message
        except HttpRequestError as e:
            error_message = (
                f"Error: "
                f"Failed to send post collection "
                f"with id '{post_collection.post_collection_id}' "
                f"(Source: '{post_collection.source.source_type.value}' "
                f"| '{post_collection.source.url}') "
                f"to the extraction service! "
                f"Error message: {e}"
            )
            logger.error(error_message)
            return error_message, None

    def transform_status_dtos_to_post_collection(
        self, source: Source, statuses: list, mapping_function: callable
    ) -> PostCollection:
        """
        Transform the given list of status DTOs to a post collection.
        :param source: Source of the post collection
        :param statuses: List of status DTOs to transform
        :param mapping_function: Function to map the status DTOs to posts (post_collection dto)
        :return: Created Post collection with the given source and transformed statuses
        """

        logger.info(f"Transforming {len(statuses)} statuses to a post collection!")
        post_collection = PostCollection(
            trigger_timestamp=self.trigger_timestamp, source=source
        )

        for status in statuses:
            post_dto = mapping_function(status)
            post_collection.posts.append(post_dto)

        logger.info(
            f"Successfully transformed {len(statuses)} statuses to the post collection "
            f"with id '{post_collection.post_collection_id}' "
            f"(Source: '{post_collection.source.source_type.value}' "
            f"| '{post_collection.source.url}')!"
        )
        return post_collection

    @staticmethod
    def check_required_keys_in_json_object(
        json_object: dict, required_keys: list
    ) -> bool:
        """
        Check if all required keys are present.
        :param json_object: JSON object to check for required keys
        :param required_keys: List of required keys that need to be present in the JSON object
        :return: True if all required values are present, False otherwise
        """

        missing_keys = []

        for key in required_keys:
            if key not in json_object:
                missing_keys.append(f"Missing '{key}' in {json_object}!")

        if missing_keys:
            for error_msg in missing_keys:
                logger.error(error_msg)
            return False

        return True

    def load_social_media_details(self, social_media_name):
        """
        Load social media details from the config file.
        :param social_media_name: Name of the social media to load details for (e.g. "MASTODON")
        :return: Social media details if found in the config file, None otherwise
        """

        social_media_name = social_media_name.upper()

        try:
            resources_dir = self.config_reader.read_config("GENERAL", "RESOURCES_DIR")
            details_dir = self.config_reader.read_config(
                social_media_name, "DETAILS_FILE_DIR"
            )
            details_file_name = self.config_reader.read_config(
                social_media_name, "DETAILS_FILE_NAME"
            )
        except ConfigReaderError as e:
            logger.error(f"Error: {e}")
            return []

        try:
            dir_path = os.path.dirname(os.path.realpath(__file__))
            details_file = os.path.join(
                dir_path,
                "..",
                "..",
                resources_dir,
                details_dir,
                details_file_name,
            )
            if not os.path.isfile(details_file):
                logger.error(
                    f'Error: File "{resources_dir}/{details_dir}/{details_file}" not found.'
                )
                return []

            with open(details_file, "r", encoding="utf-8") as file:
                return json.load(file)

        except (FileNotFoundError, json.JSONDecodeError) as e:
            logger.error(f"Error: Loading file with mastodon instance details: {e}")
            return []
