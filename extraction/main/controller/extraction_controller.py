import json

from flask import Blueprint, request, Response

from main.models.scraping.post_collection import PostCollection
from main.service.send_core_service import SendCoreService
from main.service.sentiment_service import SentimentService
from main.utils.base_logger import get_logger
from main.utils.body_validator import validate_body

logger = get_logger("extraction_controller")

extraction_controller_bp = Blueprint("extraction_controller_bp", __name__)

LOCAL_BASE_URL_SUFFIX = "/api/v1/"

send_recognition_service = SendCoreService()
sentiment_service = SentimentService()


@extraction_controller_bp.route(f"{LOCAL_BASE_URL_SUFFIX}/extract", methods=["POST"])
def extract_data() -> Response:
    data_from_scraper = request.get_json()
    logger.info("Starting with data extraction...")

    err = validate_body(data_from_scraper, ["post_collection_id", "posts", "source", "created_at",
                                            "trigger_timestamp"])
    if err is not None:
        logger.error(f"ERROR when validating incoming request body: {err.message}")
        return Response(err.to_json(), status=400)

    pc: PostCollection = PostCollection.from_dict(data_from_scraper)
    filtered_posts = sentiment_service.vader_analysis(pc.posts)
    pc.posts = filtered_posts

    if not filtered_posts:
        logger.info("No appropriate posts found")
        return Response("No appropriate posts found", status=200)
    (
        error_log,
        info_log,
    ) = send_recognition_service.send_post_collection_to_core(pc)

    if error_log and info_log:
        return Response(
            status=207,
            response=json.dumps({"error": error_log, "info": info_log}),
        )
    if error_log:
        return Response(status=500, response=json.dumps({"error": error_log}))

    logger.info("Successfully finished data extraction...")
    return Response(status=200, response=json.dumps(
        {"info": info_log}
    ))
