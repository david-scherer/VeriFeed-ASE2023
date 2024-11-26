import unittest

from main.mapper.post_mapper import PostMapper
from main.models.source import SourceType, Source
from main.service.reddit_scrape_service import RedditScrapeService
from tests.test_base_scrape import TestBaseScraper


class TestRedditScraper(TestBaseScraper, unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        super(TestRedditScraper, cls).set_up_class()

        # Variables for BaseScraper #
        # ------------------------- #

        cls.scraper_service = RedditScrapeService()

        cls.mapping_function = staticmethod(PostMapper.map_reddit_post_to_dto)

        cls.expected_source_type = SourceType.REDDIT
        cls.correct_source = Source(
            url="https://www.reddit.com/r/Austria",
            source_type=SourceType.REDDIT,
        )

        cls.required_instance_details = ["subreddits"]

        cls.correct_test_instance = {"subreddits": ["Austria"]}

        cls.incorrect_test_instance = {
            "name": "Austria",
        }

        cls.trend_status = {
            "id": "18xhsnz",
            "permalink": "/r/Austria/comments/18xhsnz"
            "/ams_chatbot_entwicklungskosten_einige_100000_euro/",
            "title": "Krieg in der Ukraine - weiterhin nicht nachlassen!",
            "selftext": "Der Angriffskrieg Russlands auf die und in der Ukraine dauert...",
            "link_flair_text": "Zahlen & Grafiken | Stats",
            "subreddit_name_prefixed": "r/Austria",
            "author": {
                "name": "TEST AUTHOR",
                "id": "AUTHOR_18xhsnz",
                "created_utc": 1700204590.0,
            },
            "score": 48,
            "num_comments": 10,
            "num_crossposts": 0,
            "created_utc": 1700204590.0,
            "edited": False,
            "media_metadata": {
                "99pmd8puczac1": {
                    "e": "Image",
                    "id": "99pmd8puczac1",
                    "m": "image/jpg",
                    "s": {
                        "u": "https://preview.redd.it/99pmd8puczac1.jpg?width=1080&format=pjpg&auto=webp&s=ca3e4dbf712ecd59f2d555fadcdb0467f774bb8f",
                        "x": 1080,
                        "y": 1816,
                    },
                    "status": "valid",
                }
            },
        }

        # ------------------------- #
        # ------------------------- #

        if __name__ == "__main__":
            unittest.main()
