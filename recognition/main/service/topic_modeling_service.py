from .dashboard_page_provider import compose_page
from main.models.similarity.post import Post
from main.models.topic_modeling.topic import Topic
from main.service.topic_modeling.BERTopic_model import BERTopicModel
from main.utils.base_logger import get_logger

logger = get_logger("topic_modeling")


class TopicModelingService:
    def __init__(self):
        self.topic_model = BERTopicModel()
        if not self.topic_model.load_model():
            logger.info("no model found - train new one")
            self.topic_model.train_model()
            self.topic_model.store_model()

    def assign_topics(self, posts: list[list[Post]], skip_learn: bool = False) -> list[list[Post]]:
        return self.topic_model.label_and_learn(posts, skip_learn)

    def get_topics(self) -> list[Topic]:
        return self.topic_model.compose_topics()


    def create_dashboard(self):
        self.topic_model.create_dashboard_sub_pages()
        return compose_page()