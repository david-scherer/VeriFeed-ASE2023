from bertopic import BERTopic
import os

from main.service.topic_modeling.loading.local_model_loader import LocalModelLoader
from main.utils.base_logger import get_logger

logger = get_logger("bert_local_model_loader")


class BERLocalModelLoader(LocalModelLoader):

    def __init__(self):
        super().__init__()

    def load_model(self, model_name: str) -> BERTopic | None:
        try:
            model_path = os.path.join(self.resources_location, model_name)
            loaded_model = BERTopic.load(model_path)
            return loaded_model
        except Exception as e:
            logger.warn(f"Error loading BERTopic model: {str(e)}")
            return None

    def store_model(self, model: BERTopic, model_name: str):
        model_path = os.path.join(self.resources_location, model_name)
        try:
            model.save(model_path, serialization="pickle")
        except Exception as e:
            logger.warn(f"Error storing BERTopic model: {str(e)}")
