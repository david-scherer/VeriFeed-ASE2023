import nltk
from nltk.corpus import stopwords

from main.models.scraping.post import Post

nltk.download('stopwords')

class Preprocessor:
    def __init__(self):
        pass

    def preprocess(self, posts: [Post]) -> [Post]:
        pass

    def preprocess_data(self, training_set: [any]) -> [any]:
        pass

    def _seed_words_(self):
        return [
            "politics",
            "economy",
            "party",
            "war",
            "conflict",
            "democrats",
            "republicans",
            "diplomacy",
            "climate",
            "democracy",
            "voting"
        ]

    def custom_stop_words(self):
        custom_stopwords = stopwords.words("english")
        custom_stopwords += [
            "https",
            "co",
            "subscribe",
            "com",
            "today",
            "get",
            "should"
        ]
        return custom_stopwords

    def _custom_stop_ids_(self, dict):
        custom_stopwords = self.custom_stop_words()
        stop_ids = [dict.token2id[stopword] for stopword in custom_stopwords
                    if stopword in dict.token2id]
        return stop_ids