from flask import Blueprint

from main.utils.base_logger import get_logger
from ..service.reddit_scrape_service import RedditScrapeService

logger = get_logger("reddit_controller")

reddit_controller_bp = Blueprint("reddit_controller_bp", __name__)

LOCAL_BASE_URL_SUFFIX = "/api/v1/reddit"


reddit_service = RedditScrapeService()


@reddit_controller_bp.route(f"{LOCAL_BASE_URL_SUFFIX}/scrape", methods=["GET"])
def scrape_reddit_direct_trending_statuses():
    logger.info("Scraping trending statuses in all subreddits triggered!")
    return reddit_service.scrape()
