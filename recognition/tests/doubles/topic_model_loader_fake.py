from bertopic import BERTopic

from main.service.topic_modeling.loading.local_model_loader import LocalModelLoader


class TopicModelLoaderFake(LocalModelLoader):

    def __init__(self):
        super().__init__()

    def load_model(self, model_name: str) -> BERTopic | None:
        print("Fake model load")
        return None

    def store_model(self, model: BERTopic, model_name: str):
        print("Fake storing model")
