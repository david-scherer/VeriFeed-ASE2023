import json
import os

from flask import Blueprint, request, jsonify, Response, render_template

from main.models.scraping.post_collection import PostCollection
from main.models.similarity.post import Post
from main.models.similarity.post_collection import (
    PostCollection as OutputPostCollection,
)
from main.service.similarity_service import SimilarityService
from main.service.topic_modeling_service import TopicModelingService
from main.utils.base_logger import get_logger
from main.utils.body_validator import validate_body
import json

logger = get_logger("recognition_controller")

recognition_controller_bp = Blueprint("recognition_controller_bp", __name__)

LOCAL_BASE_URL_SUFFIX = "/api/v1"

similarity_service = SimilarityService()
topic_modeling_service = TopicModelingService()


@recognition_controller_bp.route(
    f"{LOCAL_BASE_URL_SUFFIX}/recognition", methods=["POST"]
)
def recognize_similar_texts() -> Response:
    request_body = request.get_json()

    # Ensure that the required values exist
    err = validate_body(request_body, ["post_collection", "threshold"])
    if err is not None:
        logger.error(err.message)
        return Response(err.to_json(), status=400, content_type="application/json")

    post_collection = request_body["post_collection"]
    threshold = request_body["threshold"]

    post_collection = PostCollection.from_dict(post_collection)
    skip_learn = request.args.get("skipLearn", False)

    labeled_post_groups = topic_modeling_service.assign_topics(
        similarity_service.group_posts(post_collection.posts, threshold),
        skip_learn=skip_learn,
    )
    if not labeled_post_groups:
        logger.info("No labeled post groups to send to the core service!")
        return Response(status=200)

    logger.info("Labeled post groups contains %s groups!", len(labeled_post_groups))

    output_post_collection = OutputPostCollection(
        trigger_timestamp=post_collection.trigger_timestamp,
        post_collection_id=post_collection.post_collection_id,
        posts=labeled_post_groups,
        source=post_collection.source,
        created_at=post_collection.created_at,
    )

    topics = topic_modeling_service.get_topics()

    resp = json.dumps(
        {
            "output_post_collection": output_post_collection.to_dict(),
            "topics": [topic.to_dict() for topic in topics],
        }
    )

    return Response(
        status=200,
        content_type="application/json",
        response=resp,
    )


@recognition_controller_bp.route("/dashboard")
def get_dashboard():
    try:
        os.mkdir("recognition/templates", mode=0o666)
    except FileExistsError:
        pass
    with open("recognition/templates/dashboard.html", "w") as file:
        dashboard_content = topic_modeling_service.create_dashboard()
        file.writelines(dashboard_content)
    return render_template("dashboard.html")


@recognition_controller_bp.route(f"{LOCAL_BASE_URL_SUFFIX}/topic", methods=["GET"])
def assign_topic_to_post() -> Response:
    request_body = request.get_json()

    # Ensure that the required values exist
    err = validate_body(request_body, ["post"])
    if err is not None:
        logger.error(err.message)
        return Response(err.to_json(), status=400)

    post = request_body["post"]
    post = Post.from_dict(post)

    post = topic_modeling_service.assign_topics([[post]])[0][0]
    return jsonify({"post": post})
