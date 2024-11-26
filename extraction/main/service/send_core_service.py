import requests
from requests import Response

from main.models.http_request_error import HttpRequestError
from main.models.scraping.post_collection import PostCollection
from main.utils.base_logger import get_logger
from main.utils.config_reader import ConfigReader

logger = get_logger("send_core_service")


class SendCoreService:
    def __init__(self):
        self.config_reader = ConfigReader()
        self.headers = {"Content-Type": "application/json"}

    def send_post_collection_to_core(
            self, post_collection: PostCollection
    ) -> tuple:
        try:
            core_endpoint_url = self.config_reader.read_config(
                "Core", "CORE_POST_ENDPOINT"
            )
        except (FileNotFoundError, KeyError) as e:
            error_message = f"Error: {e}"
            logger.error(error_message)
            return error_message, None

        if len(post_collection.posts) == 0:
            info_log = "Posts were empty"
            return None, info_log

        try:
            response = self.__make_http_request(
                "POST",
                url=core_endpoint_url,
                json_data=post_collection.to_dict()
            )
            info_message = (
                f"Successfully sent post collection "
                f"with id '{post_collection.post_collection_id}' "
                f"(Source: '{post_collection.source.source_type.value}' "
                f"| '{post_collection.source.url}') "
                f"to the core! "
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
                f"to the core! "
                f"Error message: {e}"
            )
            logger.error(error_message)
            return error_message, None

    def __make_http_request(self, method, url, params=None,
                            json_data=None) -> Response | HttpRequestError:
        response = None
        try:
            response = requests.request(
                method,
                url,
                headers=self.headers,
                json=json_data,
                params=params,
                timeout=300,
            )
            response.raise_for_status()
            return response

        except requests.RequestException as e:
            raise HttpRequestError({e}, response) from e
