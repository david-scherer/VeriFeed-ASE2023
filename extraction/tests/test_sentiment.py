import unittest
from datetime import datetime
from main.models.scraping.post import Post
from main.models.scraping.account import Account
from main.models.scraping.post_metrics import PostMetrics
from main.service.sentiment_service import SentimentService

class MyTest(unittest.TestCase):

    @classmethod
    def setUp(self):
        self.dummy_posts = [
            Post(
                post_id="12345",
                url="https://www.facebook.com/post/12345",
                created_at=datetime(2023, 10, 4, 12, 30, 0),
                account=Account(username="johndoe",account_id="test",created_at=datetime(2023, 10, 4, 12, 30, 0)),
                content="Just had the most amazing meal at Johns! The food was delicious, the service was great, and the ambiance was perfect. I highly recommend checking it out!",
                metrics=PostMetrics(reposts_count=150, favourites_count=30, replies_count=10),
                parent_post=None,
                hashtags=[],
                media_attachments=[],
                edited=False,
            ),
            Post(
                post_id="67890",
                url="https://www.twitter.com/johndoe/status/67890",
                created_at=datetime(2023, 10, 3, 17, 45, 0),
                account=Account(username="johndoe",account_id="test",created_at=datetime(2023, 10, 4, 12, 30, 0)),
                content="I'm so excited to announce that I'm starting a new job at google! I'm thrilled to be part of such an innovative and exciting team.",
                metrics=PostMetrics(reposts_count=150, favourites_count=30, replies_count=10),
                parent_post=None,
                hashtags=[],
                media_attachments=[],
                edited=True,
            ),
            Post(
                post_id="123456",
                url="https://www.instagram.com/johndoe/p/123456",
                created_at=datetime(2023, 9, 29, 21, 00, 0),
                account=Account(username="johndoe",account_id="test",created_at=datetime(2023, 10, 4, 12, 30, 0)),
                content="The view from the top of mnt fuju is absolutely breathtaking! #mountainview #hikingadventure",
                metrics=PostMetrics(reposts_count=150, favourites_count=30, replies_count=10),
                parent_post=None,
                hashtags=[],
                media_attachments=[],
                edited=False,
            ),
            Post(
                post_id="123456",
                url="https://www.instagram.com/johndoe/p/123456",
                created_at=datetime(2023, 9, 29, 21, 00, 0),
                account=Account(username="johndoe",account_id="test",created_at=datetime(2023, 10, 4, 12, 30, 0)),
                content="I'm so sick of COVID-19 and this government! It sucks staying at home for no actual reason :(",
                metrics=PostMetrics(reposts_count=150, favourites_count=30, replies_count=10),
                parent_post=None,
                hashtags=[],
                media_attachments=[],
                edited=False,
            ),
            Post(
                post_id="123456",
                url="https://www.instagram.com/johndoe/p/123456",
                created_at=datetime(2023, 9, 29, 21, 00, 0),
                account=Account(username="johndoe",account_id="test",created_at=datetime(2023, 10, 4, 12, 30, 0)),
                content="Several factors have contributed to the decline in COVID-19 cases, including the widespread availability of vaccines and the increasing immunity of populations. Vaccination rates have risen significantly in many countries, particularly among the most vulnerable groups. Additionally, the Omicron variant, which is generally less severe than earlier variants, has fueled a wave of infections that has resulted in less severe illness and fewer deaths.",
                metrics=PostMetrics(reposts_count=150, favourites_count=30, replies_count=10),
                parent_post=None,
                hashtags=[],
                media_attachments=[],
                edited=False,
            ),
            Post(
                post_id="123456",
                url="https://www.instagram.com/johndoe/p/123456",
                created_at=datetime(2023, 9, 29, 21, 00, 0),
                account=Account(username="johndoe",account_id="test",created_at=datetime(2023, 10, 4, 12, 30, 0)),
                content="This is a post",
                metrics=PostMetrics(reposts_count=150, favourites_count=30, replies_count=10),
                parent_post=None,
                hashtags=[],
                media_attachments=[],
                edited=False,
            ),
        ]
        self.too_short_posts = [
            Post(
                post_id="123456",
                url="https://www.instagram.com/johndoe/p/123456",
                created_at=datetime(2023, 9, 29, 21, 00, 0),
                account=Account(username="johndoe",account_id="test",created_at=datetime(2023, 10, 4, 12, 30, 0)),
                content="This is a post",
                metrics=PostMetrics(reposts_count=150, favourites_count=30, replies_count=10),
                parent_post=None,
                hashtags=[],
                media_attachments=[],
                edited=False,
            ),
            Post(
                post_id="123456",
                url="https://www.instagram.com/johndoe/p/123456",
                created_at=datetime(2023, 9, 29, 21, 00, 0),
                account=Account(username="johndoe",account_id="test",created_at=datetime(2023, 10, 4, 12, 30, 0)),
                content="I hate politics and what is happening rn",
                metrics=PostMetrics(reposts_count=150, favourites_count=30, replies_count=10),
                parent_post=None,
                hashtags=[],
                media_attachments=[],
                edited=False,
            ),
        ]
        self.subjective_posts = [
            Post(
                post_id="123456",
                url="https://www.instagram.com/johndoe/p/123456",
                created_at=datetime(2023, 9, 29, 21, 00, 0),
                account=Account(username="johndoe",account_id="test",created_at=datetime(2023, 10, 4, 12, 30, 0)),
                content="Life is just so damn perfect right now! I'm so grateful for all the love and happiness in my life. Love y'all",
                metrics=PostMetrics(reposts_count=150, favourites_count=30, replies_count=10),
                parent_post=None,
                hashtags=[],
                media_attachments=[],
                edited=False,
            ),
            Post(
                post_id="123456",
                url="https://www.instagram.com/johndoe/p/123456",
                created_at=datetime(2023, 9, 29, 21, 00, 0),
                account=Account(username="johndoe",account_id="test",created_at=datetime(2023, 10, 4, 12, 30, 0)),
                content="I'm having the worst day ever! Everything is going wrong and I just want to crawl into a hole and disappear.",
                metrics=PostMetrics(reposts_count=150, favourites_count=30, replies_count=10),
                parent_post=None,
                hashtags=[],
                media_attachments=[],
                edited=False,
            ),
        ]

    def test_sentiment_based_filtering(self):
        filtered_posts = SentimentService().vader_analysis(posts=self.dummy_posts)      
        self.assertLess(len(filtered_posts), len(self.dummy_posts))

    def test_filter_out_posts_with_less_than_10_words(self):
        filtered_posts = SentimentService().vader_analysis(posts=self.too_short_posts)      
        self.assertEqual(len(filtered_posts), 0)

    def test_filter_out_too_subjective_posts(self):
        filtered_posts = SentimentService().vader_analysis(posts=self.subjective_posts)      
        self.assertEqual(len(filtered_posts), 0)

if __name__ == "__main__":
    unittest.main()
