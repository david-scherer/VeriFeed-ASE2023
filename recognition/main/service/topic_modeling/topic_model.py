from main.models.scraping.post import Post

class TopicModel:
    def __init__(self):
        pass

    def get_topic(self, post: Post):
        pass

    def label_and_learn(self, posts: [Post], skip_learn: bool) -> [Post]:
        pass

    def load_model(self, force_train=False) -> bool:
        pass

    def train_model(self, posts: [Post]):
        pass

    def store_model(self):
        pass



    # work-flow
    # 1.    at startup: service loads model from minio bucket
    # 1.1   if model does not exist -> load_model() returns false and service needs to do the following:
    # 1.2   call train_model()
    # 1.3   store_model in minio bucket
    # 2.    else: model is ready to use
    # 3.    get_topic() to get new topic for post

