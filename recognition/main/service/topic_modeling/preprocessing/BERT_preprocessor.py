from main.models.scraping.post import Post
from main.service.topic_modeling.preprocessing.preprocessor import Preprocessor


# most BERT preprocessing is handled model-intern (definition during init)
class BERTPreprocessor(Preprocessor):
    def __init__(self, training_size=10000):
        super().__init__()
        self.training_size = training_size
        pass

    def preprocess(self, posts: [Post]) -> [Post]:
        return [post.content for post in posts][:self.training_size]

    def preprocess_data(self, training_set: [str]) -> [str]:
        training_set = [" ".join([word for word in post.split() if "http" not in word and len(word) > 3]) for post in training_set]
        return training_set[:self.training_size]

