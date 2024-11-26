from flask import Blueprint

from main.utils.base_logger import get_logger
from ..service.base_scrape_service import BaseScrapeService

logger = get_logger("base_scrape_controller")

base_scrape_controller_bp = Blueprint("base_scrape_controller_bp", __name__)

LOCAL_BASE_URL_SUFFIX = "/api/v1"

base_scrape_service = BaseScrapeService()


@base_scrape_controller_bp.route(f"{LOCAL_BASE_URL_SUFFIX}/scrape", methods=["POST"])
def scrape_status_with_url():
    logger.info("Scraping status with URL triggered!")
    return base_scrape_service.scrape_single_status()
