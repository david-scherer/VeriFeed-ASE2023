import json
from urllib.parse import urlsplit

from flask import Response
from requests import JSONDecodeError

from main.utils.base_logger import get_logger
from .base_scrape_service import BaseScrapeService
from ..mapper.post_mapper import PostMapper
from ..models.errors.config_reader_error import ConfigReaderError
from ..models.errors.http_request_error import HttpRequestError
from ..models.errors.scrape_status_error import ScrapeStatusError
from ..models.source import Source, SourceType
from ..utils.scraper_util import make_http_request, scrape_single_status

TRENDS_STATUSES = "trends/statuses"
TIMELINES_TAG = "timelines/tag/"
TRENDS_TAGS = "trends/tags"
STATUS_DETAILS = "statuses/"

REQUIRED_INSTANCE_DETAILS = [
    "name",
    "api_url",
    "max_tags_per_page",
    "max_tags_total",
    "max_statuses_per_page",
    "max_statuses_total",
]

HEADERS = {"Content-Type": "application/json"}

logger = get_logger("mastodon_scrape_service")


class MastodonScrapeService(BaseScrapeService):
    """
    Service for scraping trending statuses from Mastodon instances.

    The service can be configured to either use Mastodon's trending tags or
    Verifeed's own definition of trending statuses. The latter is achieved by
    fetching trending tags from the Mastodon instance and then fetching
    statuses containing those tags. The statuses with a calculated popularity
    above a certain threshold are then returned as trending statuses.

    The service can be configured to scrape multiple Mastodon instances.
    """

    def __init__(self, statuses_with_tags):
        super().__init__()
        self.headers = HEADERS
        self.statuses_with_tags = statuses_with_tags
        self.mastodon_instances_as_json = self.load_social_media_details("MASTODON")

    def scrape(self):
        if not self.mastodon_instances_as_json:
            error_message = "No mastodon instances found in the configuration file!"
            logger.error(f"Error: {error_message}")
            return Response(status=404, response=error_message)

        post_collections = []
        for instance in self.mastodon_instances_as_json:
            if not self.check_required_keys_in_json_object(
                instance, REQUIRED_INSTANCE_DETAILS
            ):
                continue

            logger.info(f'Scraping triggered for instance "{instance["name"]}"!')

            # Procedure for fetching trending statuses can be chosen to either
            # use Mastodon's trending tags or Verifeed's own definition of
            # trending statuses.
            if self.statuses_with_tags:
                total_trending_statuses = self.get_total_own_trending_statuses(instance)
            else:
                total_trending_statuses = self.fetch_all_page_items(
                    instance_name=instance["name"],
                    fetch_url=f'{instance["api_url"]}{TRENDS_STATUSES}',
                    max_items_per_page=instance["max_statuses_per_page"],
                    max_items_total=instance["max_statuses_total"],
                )

            if not total_trending_statuses:
                logger.warning(
                    f"No trending statuses for instance "
                    f'"{instance["name"]}" ({instance["api_url"]})!'
                )
                continue

            url = (
                urlsplit(instance["api_url"]).scheme
                + "://"
                + urlsplit(instance["api_url"]).netloc
            )
            source = Source(url=url, source_type=SourceType.MASTODON)

            total_trending_statuses = self.add_additional_scraping_fields_to_statuses(
                api_url=instance["api_url"], statuses=total_trending_statuses
            )

            post_collection = self.transform_status_dtos_to_post_collection(
                source=source,
                statuses=total_trending_statuses,
                mapping_function=PostMapper.map_mastodon_post_to_dto,
            )
            post_collections.append(post_collection)

            logger.info(
                f"Scraping finished for mastodon instance '{instance['name']}'! "
                f"Number of trending statuses: {len(total_trending_statuses)}"
            )

        return self.send_post_collections_to_extraction_service(post_collections)

    def add_additional_fields_to_status(
        self, status: dict, api_url: str = None
    ) -> dict:
        if status["in_reply_to_id"] is not None:
            try:
                status_details = scrape_single_status(
                    headers=self.headers,
                    status_url=f"{api_url}"
                    f'{STATUS_DETAILS}{status["in_reply_to_id"]}',
                )

                status["additional_scraping_fields"] = {
                    "parent_post": {
                        "parent_post_id": status_details["id"],
                        "parent_post_url": status_details["url"],
                    }
                }
            except ScrapeStatusError as e:
                logger.error(f"Error: {e}")
        return status

    def get_total_own_trending_statuses(self, instance):
        """
        Fetches trending (according to Verifeed's definition) statuses
        containing trending (according to Mastodon) tags.
        :param instance: Instance details of the Mastodon instance
        :return: List of trending statuses containing trending tags
        with computed popularity above the threshold
        """

        total_trending_statuses = []

        total_trending_tags = self.fetch_all_page_items(
            instance_name=instance["name"],
            fetch_url=f'{instance["api_url"]}{TRENDS_TAGS}',
            max_items_per_page=instance["max_tags_per_page"],
            max_items_total=instance["max_tags_total"],
        )
        if not total_trending_tags:
            logger.warning(
                f"No trending tags for instance "
                f'"{instance["name"]}" {instance["api_url"]}!'
            )
            return []

        try:
            popularity_threshold = float(
                self.config_reader.read_config("MASTODON", "POPULARITY_THRESHOLD")
            )
        except ConfigReaderError as e:
            logger.error(f"Error: {e}")
            popularity_threshold = 0.0

        for tag in total_trending_tags:
            total_statuses_for_tag = self.fetch_all_page_items(
                instance_name=instance["name"],
                fetch_url=f'{instance["api_url"]}{TIMELINES_TAG}{tag["name"]}',
                max_items_per_page=instance["max_statuses_per_page"],
                max_items_total=instance["max_statuses_total"],
            )
            if total_statuses_for_tag:
                filtered_trending_statuses_by_tag = list(
                    filter(
                        lambda status: self.calculate_popularity(status)
                        > popularity_threshold,
                        total_statuses_for_tag,
                    )
                )
                total_trending_statuses.extend(filtered_trending_statuses_by_tag)
            else:
                logger.warning(
                    f'No statuses for tag "{tag["name"]}" for instance "{instance["name"]}"!'
                )

        return total_trending_statuses

    def calculate_popularity(self, status):
        """
        Calculates the popularity of the given status based on the number of
        reblogs, favourites and replies.
        :param status: Status to calculate popularity for
        :return: Popularity of the given status
        """

        try:
            reblogs_weight = float(
                self.config_reader.read_config("MASTODON", "REBLOGS_WEIGHT")
            )
            favourites_weight = float(
                self.config_reader.read_config("MASTODON", "FAVOURITES_WEIGHT")
            )
            replies_weight = float(
                self.config_reader.read_config("MASTODON", "REPLIES_WEIGHT")
            )
        except ConfigReaderError as e:
            logger.error(f"Error: {e}")
            reblogs_weight = 0.0
            favourites_weight = 0.0
            replies_weight = 0.0

        return (
            status.get("reblogs_count", 0) * reblogs_weight
            + status.get("favourites_count", 0) * favourites_weight
            + status.get("replies_count", 0) * replies_weight
        )

    def fetch_all_page_items(
        self,
        instance_name: str,
        fetch_url: str,
        max_items_per_page: int,
        max_items_total: int,
    ) -> list:
        """
        Fetches all items from the given endpoint. The API uses pagination and
        returns a given number of results per page. The function iterates
        through the pages until a limit given by the instance is reached.
        :param instance_name: Name of the Mastodon instance
        :param fetch_url: Endpoint URL to fetch items from
        :param max_items_per_page: Maximum number of items per page
        :param max_items_total: Maximum number of items in total
        :return: List of items fetched from the given endpoint
        """

        all_items = []
        offset = 0
        while len(all_items) < max_items_total:
            params = {"limit": max_items_per_page, "offset": offset}
            try:
                response = make_http_request(
                    headers=self.headers, method="GET", url=fetch_url, params=params
                )

                if response:
                    page_items = response.json()
                    all_items.extend(page_items)

                    if len(page_items) < max_items_per_page:
                        break

                    offset += max_items_per_page
            except HttpRequestError as e:
                logger.error(
                    f"Failed to fetch for instance '{instance_name}' "
                    f"('{fetch_url}') with params '{params}'! "
                    f"Error message: {e}"
                )
                return all_items
            except JSONDecodeError as e:
                logger.error(f"Response is not a valid json!" f"Error message: {e}")

        return all_items

    def scrape_status_with_url(self, url: str) -> Response:
        logger.info(f"Scraping mastodon status with URL '{url}' triggered!")

        try:
            status_id = url.split("/")[-1]
            base_url = f"{urlsplit(url).scheme}://{urlsplit(url).netloc}/"
            api_url = f"{base_url}api/v1/"

            status = scrape_single_status(
                headers=self.headers,
                status_url=f"{api_url}" f"{STATUS_DETAILS}{status_id}",
            )

            source = Source(url=base_url, source_type=SourceType.MASTODON)

            statuses = self.add_additional_scraping_fields_to_statuses(
                api_url=api_url, statuses=[status]
            )

            post_collection = self.transform_status_dtos_to_post_collection(
                source=source,
                statuses=statuses,
                mapping_function=PostMapper.map_mastodon_post_to_dto,
            )

            logger.info(f"Scraping mastodon status with URL '{url}' finished!")

            return self.send_post_collections_to_extraction_service([post_collection])
        except ScrapeStatusError as e:
            logger.error(f"Error: {e}")
            return Response(
                status=500, response=json.dumps({"error": "Scraping status failed!"})
            )
        except Exception as e:
            logger.error(f"Unexpected error: {e}")
            return Response(
                status=500, response=json.dumps({"error": "Scraping status failed!"})
            )
