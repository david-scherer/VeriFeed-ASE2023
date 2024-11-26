# from main.utils.vader.custom_vader import SentimentIntensityAnalyzer as CustomSentimentIntensityAnalyzer
from main.utils.vader.custom_vader import SentimentIntensityAnalyzer
from main.utils.base_logger import get_logger

logger = get_logger("sentiment_service")

NEUTRALITY_THRESHOLD = 0.65
COMPOUND_THRESHOLD = 0.5
MIN_WORD_COUNT = 10

class SentimentService:
    def _preprocess(self, text):
        new_text = []
        word_count = len(text.split(" "))
        for t in text.split(" "):
            t = '@user' if t.startswith('@') and len(t) > 1 else t
            t = 'http' if t.startswith('http') else t
            new_text.append(t)
        return " ".join(new_text), word_count
            
    # a post is usually classified as neutral if -0.05 <= compound score <= 0.05
    # due to natural subjectivness of social media posts, we soften the threshold
    # for classifying a post as relevant for our application
    # https://github.com/cjhutto/vaderSentiment
    def vader_analysis(self, posts):
        sia = SentimentIntensityAnalyzer()
        filtered = []

        for post in posts:
            preprocessed_text, word_count = self._preprocess(post.content)

            if (word_count < MIN_WORD_COUNT):
                logger.debug(f"Post does not meet minimum word count: '{post.content}'")
                continue

            scores = sia.polarity_scores(preprocessed_text)
            neu = scores['neu']
            compound = scores['compound']

            # check for neutral word count as checking for compound might sort out relevant posts
            if neu > NEUTRALITY_THRESHOLD or (-COMPOUND_THRESHOLD < compound < COMPOUND_THRESHOLD):  
                logger.debug(f"Add post with score {scores}: '{post.content}'")
                filtered.append(post)
            else:
                logger.debug(f"Filtered out post with score {scores}: '{post.content}'")
        
        return filtered
