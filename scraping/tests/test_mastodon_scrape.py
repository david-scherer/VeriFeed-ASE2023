import unittest

from main.mapper.post_mapper import PostMapper
from main.models.source import SourceType, Source
from main.service.mastodon_scrape_service import MastodonScrapeService
from tests.test_base_scrape import TestBaseScraper


class TestMastodonScraper(TestBaseScraper, unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        super(TestMastodonScraper, cls).set_up_class()

        # Variables for BaseScraper #
        # ------------------------- #

        cls.scraper_service = MastodonScrapeService(False)

        cls.mapping_function = staticmethod(PostMapper.map_mastodon_post_to_dto)

        cls.expected_source_type = SourceType.MASTODON
        cls.correct_source = Source(
            url="https://mastodon.social/api/v1/",
            source_type=SourceType.MASTODON,
        )
        cls.required_instance_details = [
            "name",
            "api_url",
            "max_tags_per_page",
            "max_tags_total",
            "max_statuses_per_page",
            "max_statuses_total",
        ]

        cls.correct_test_instance = {
            "name": "Mastodon Social",
            "api_url": "https://mastodon.social/api/v1/",
            "max_tags_per_page": 2,
            "max_tags_total": 2,
            "max_statuses_per_page": 2,
            "max_statuses_total": 2,
        }

        cls.incorrect_test_instance = {
            "max_tags_per_page": 2,
            "max_tags_total": 2,
            "max_statuses_per_page": 2,
            "max_statuses_total": 2,
        }

        cls.trend_status = {
            "id": "111404175214422126",
            "created_at": "2023-11-13T16:26:27.000Z",
            "in_reply_to_id": None,
            "url": "https://tech.lgbt/@pierogiburo/111404173721615623",
            "replies_count": 2,
            "reblogs_count": 112,
            "favourites_count": 10,
            "edited_at": None,
            "content": "<p>me lying awake in bed at 3am</p>",
            "account": {
                "id": "109457073429121397",
                "username": "pierogiburo",
                "created_at": "2022-12-04T00:00:00.000Z",
                "url": "https://tech.lgbt/@pierogiburo",
            },
            "media_attachments": [
                {
                    "id": "111404260697917770",
                    "type": "image",
                    "url": "https://files.mastodon.social/media_attachments/files/111/404/260/697"
                    "/917/770/original/072cfa74463fa51d.jpeg",
                }
            ],
            "tags": [
                {"name": "birds", "url": "https://mastodon.social/tags/birds"},
                {
                    "name": "birdsofmastodon",
                    "url": "https://mastodon.social/tags/birdsofmastodon",
                },
            ],
        }

        # ------------------------- #
        # ------------------------- #

        cls.popular_status = {
            "reblogs_count": 1000,
            "favourites_count": 1000,
            "replies_count": 1000,
        }

        cls.unpopular_status = {
            "reblogs_count": 0.0,
            "favourites_count": 0.0,
            "replies_count": 0.0,
        }

    def test_get_total_own_trending_statuses_returns_list_of_statuses(self):
        statuses = self.scraper_service.get_total_own_trending_statuses(
            self.correct_test_instance
        )
        self.assertIsInstance(statuses, list)

    def test_filter_statuses_by_popularity_filter_out_unpopular_statuses(self):
        status_popularity = self.scraper_service.calculate_popularity(
            self.unpopular_status
        )
        self.assertEqual(status_popularity, 0.0)

    def test_filter_statuses_by_popularity_dont_filter_out_popular_statuses(self):
        popularity_threshold = 50.0
        status_popularity = self.scraper_service.calculate_popularity(
            self.popular_status
        )
        self.assertGreater(status_popularity, popularity_threshold)


if __name__ == "__main__":
    unittest.main()
