import logging
import unittest
from typing import List

from main.models.scraping.post import Post
from main.service.topic_modeling.BERTopic_model import BERTopicModel
from main.service.topic_modeling.preprocessing.BERT_preprocessor import BERTPreprocessor
from main.service.topic_modeling.training.training_set_provider import TrainingSetProvider
from tests.doubles.topic_model_loader_fake import TopicModelLoaderFake
from tests.doubles.topic_update_counter_fake import LocalTopicUpdateCounterFake

num_batches = 5
training_set_size = 5000
validation_set_size = 500
meta_num_topics = 5


class TestSimilarityService(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        data_set = TrainingSetProvider().load_data_set()
        data_set = BERTPreprocessor().preprocess_data(data_set)
        cls.training_set = data_set[:training_set_size]
        cls.validation_set = data_set[training_set_size + 1: training_set_size + 1 + validation_set_size]

    def compose_posts(self, set: List[str]) -> List[Post]:
        fake_posts = []
        for item in set:
            fake_posts.append(Post(
                post_id="",
                url="",
                created_at=None,
                account=None,
                content=item,
                metrics=None,
                parent_post=None,
                hashtags=[],
                media_attachments=[],
                edited=False,
                topic=-1,
            ))
        return fake_posts

    def test_online_learning_label_posts(self):
        logger = logging.getLogger()
        logger.setLevel(logging.NOTSET)
        model = BERTopicModel(
            topic_update_counter=LocalTopicUpdateCounterFake(),
            model_loader=TopicModelLoaderFake(),
            logger=logger
        )
        fake_posts = self.compose_posts(self.training_set)
        batch_size = int(training_set_size / num_batches)

        for i in range(num_batches):
            batch = fake_posts[i * batch_size: (i + 1) * batch_size]
            model.label_and_learn([batch], skip_learn=False)

        for post in fake_posts:
            self.assertNotEqual(post.topic, -1)

    def test_online_learning_get_new_topics_incrementally(self):
        logger = logging.getLogger()
        logger.setLevel(logging.NOTSET)
        model = BERTopicModel(
            topic_update_counter=LocalTopicUpdateCounterFake(),
            model_loader=TopicModelLoaderFake(),
            logger=logger
        )
        fake_posts = self.compose_posts(self.training_set)
        batch_size = int(training_set_size / num_batches)

        topics = []
        for i in range(num_batches):
            batch = fake_posts[i * batch_size: (i + 1) * batch_size]
            model.label_and_learn([batch], skip_learn=False)
            new_topics = model.compose_topics()
            for new_topic in new_topics:
                self.assertFalse(new_topic in topics)
                topics.append(new_topic)
            print(f"number of new topics: {len(new_topics)}")
        for topic in topics:
            print(topic.to_dict())



    def test_hierarchical_topic_modelling(self):
        logger = logging.getLogger()
        logger.setLevel(logging.NOTSET)
        model = BERTopicModel(
            topic_update_counter=LocalTopicUpdateCounterFake(),
            model_loader=TopicModelLoaderFake(),
            logger=logger
        )
        fake_posts = self.compose_posts(self.training_set)
        batch_size = int(training_set_size / num_batches)

        topics = []

        for i in range(num_batches):
            batch = fake_posts[i * batch_size: (i + 1) * batch_size]
            model.label_and_learn([batch], skip_learn=False)
            topics.extend(model.model.topics_)

        model.model.topics_ = topics

        post_content = [post.content for post in fake_posts]
        hierarchical_topics = model.model.hierarchical_topics(post_content)
        print(hierarchical_topics)
        fig = model.model.visualize_hierarchy(hierarchical_topics=hierarchical_topics)
        fig.write_html("hierarchy.html")

    def test_metamorphic_modeling(self):
        logger = logging.getLogger()
        logger.setLevel(logging.NOTSET)
        model = BERTopicModel(
            topic_update_counter=LocalTopicUpdateCounterFake(),
            model_loader=TopicModelLoaderFake(),
            logger=logger
        )
        fake_posts = self.compose_posts(self.training_set)
        model.label_and_learn([fake_posts], skip_learn=False)

        validation_posts = self.compose_posts(self.validation_set)
        topics = model.model.get_topics()
        for post in validation_posts:
            for id, list in [(key, value) for key, value in topics.items() if key in range(meta_num_topics)]:
                # get distribution of unmodified post
                # modify post with typical topic words
                # get distribution of unmodified post
                # assert distribution of inserted topics has risen
                dist, _ = model.model.approximate_distribution(documents=[post.content])
                modified_content = post.content + " ".join([word[0] for word in list])
                mod_dist, _ = model.model.approximate_distribution(documents=[modified_content])
                self.assertLessEqual(dist[0][id], mod_dist[0][id])
