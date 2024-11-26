import datetime
import random
import unittest
import uuid

from faker import Faker

from main.models.scraping.post import Post
from main.models.scraping.post_collection import PostCollection
from main.service.similarity_service import SimilarityService


class TestSimilarityService(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        Faker.seed(0)
        cls.fake = Faker()

        cls.similarity_service = SimilarityService()

        cls.post_1 = Post(
            str(uuid.uuid4()),
            "",
            datetime.datetime.now(),
            None,
            "This is a sentence that contains a bit of text",
            None,
            "",
            [],
            [],
            False,
        )
        cls.post_2 = Post(
            str(uuid.uuid4()),
            "",
            datetime.datetime.now(),
            None,
            "Tralalalala",
            None,
            "",
            [],
            [],
            False,
        )
        cls.post_lorem_ipsum_1 = Post(
            str(uuid.uuid4()),
            "",
            datetime.datetime.now(),
            None,
            "Quia id qui fugiat suscipit aliquam. Consequatur neque et facere. Aliquam eos accusamus sed amet consequatur sed.",
            None,
            "",
            [],
            [],
            False,
        )

    def test_group_texts_without_similarities_return_3_groups(self):
        posts_input = PostCollection(
            datetime.datetime.now(),
            str(uuid.uuid4()),
            [
                self.post_1,
                self.post_2,
                self.post_lorem_ipsum_1,
            ],
            None,
            datetime.datetime.now(),
        )
        post_collection = self.similarity_service.group_posts(
            posts_input,
            0.1,
        )

        self.assertEqual(post_collection.trigger_timestamp, posts_input.trigger_timestamp)
        self.assertEqual(post_collection.post_collection_id, posts_input.post_collection_id)
        self.assertTrue(len(post_collection.posts) == len(posts_input.posts))
        self.assertEqual(post_collection.source, posts_input.source)
        self.assertEqual(post_collection.created_at, posts_input.created_at)

    def test_group_text_with_n_similar_posts_return_n_groups(self):
        posts_input = PostCollection(
            datetime.datetime.now(),
            str(uuid.uuid4()),
            [
                self.post_1,
                self.post_2,
            ],
            None,
            datetime.datetime.now(),
        )
        num_similar_elements = random.randint(3, 16)
        text = self.fake.sentence()
        for _ in range(num_similar_elements):
            # Swap two random words
            text_split = text.split(" ")
            num_words = len(text_split)
            pos1 = random.randint(0, num_words - 1)
            pos2 = random.randint(0, num_words - 1)
            word1 = text_split[pos1]
            word2 = text_split[pos2]
            text_split[pos1] = word2
            text_split[pos2] = word1
            text_changed = " ".join(text_split)

            posts_input.posts.append(
                Post(
                    str(uuid.uuid4()),
                    "",
                    datetime.datetime.now(),
                    None,
                    text_changed,
                    None,
                    "",
                    [],
                    [],
                    False,
                )
            )

        post_collection = self.similarity_service.group_posts(posts_input, 0.3)

        self.assertTrue(len(post_collection.posts) == 3)

        # Ensure that largest group (the one with random elements contains enough elements)
        max_elems = 0
        largest_group = None
        for post_group in post_collection.posts:
            if len(post_group) > max_elems:
                largest_group = post_group
                max_elems = len(post_group)
        self.assertTrue(len(largest_group) == num_similar_elements)

    def test_empty_input_return_empty_groups(self):
        posts_input = PostCollection(
            datetime.datetime.now(),
            str(uuid.uuid4()),
            [],
            None,
            datetime.datetime.now(),
        )
        post_collection = self.similarity_service.group_posts(
            posts_input,
            0.2,
        )
        self.assertTrue(len(post_collection.posts) == 0)


if __name__ == "__main__":
    unittest.main()
