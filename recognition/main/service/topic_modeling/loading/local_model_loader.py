import os

from main.service.topic_modeling.loading.model_loader import ModelLoader


class LocalModelLoader(ModelLoader):
    def __init__(self):
        dir_path = os.path.dirname(os.path.realpath(__file__))
        self.resources_location = os.path.join(dir_path, "..", "..", "..", "..", "resources")
        super().__init__()

    def load_model(self, model_name: str):
        pass

    def store_model(self, model, model_name: str):
        pass