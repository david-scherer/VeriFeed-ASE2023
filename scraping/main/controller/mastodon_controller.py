from flask import Blueprint

from main.utils.base_logger import get_logger
from ..service.mastodon_scrape_service import MastodonScrapeService

logger = get_logger("mastodon_controller")

mastodon_controller_bp = Blueprint("mastodon_controller_bp", __name__)

LOCAL_BASE_URL_SUFFIX = "/api/v1/mastodon"


mastodon_service = MastodonScrapeService(False)
mastodon__service_with_tags = MastodonScrapeService(True)


@mastodon_controller_bp.route(f"{LOCAL_BASE_URL_SUFFIX}/scrape", methods=["GET"])
def scrape_mastodon_direct_trending_statuses():
    logger.info("Scraping trending statuses in all mastodon instances triggered!")
    return mastodon_service.scrape()


@mastodon_controller_bp.route(f"{LOCAL_BASE_URL_SUFFIX}/scrape/tags", methods=["GET"])
def scrape_mastodon_statuses_with_trending_tags():
    logger.info(
        "Scraping statuses with trending tags in all mastodon instances triggered!"
    )
    return mastodon__service_with_tags.scrape()
