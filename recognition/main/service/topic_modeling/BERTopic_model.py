import numpy as np
from bertopic import BERTopic
from bertopic.vectorizers import ClassTfidfTransformer, OnlineCountVectorizer
from umap import UMAP

from main.models.scraping.post import Post
from main.models.topic_modeling.topic import Topic
from main.service.topic_modeling.river_clustering import River
from main.service.topic_modeling.hyperparams import bertopic_hyperparams
from main.service.topic_modeling.labeling.TopicLabelingType import TopicLabelingType
from main.service.topic_modeling.labeling.topic_labeling_factory import TopicLabelingFactory
from main.service.topic_modeling.loading.bert_local_model_loader import BERLocalModelLoader
from main.service.topic_modeling.loading.local_model_loader import LocalModelLoader
from main.service.topic_modeling.loading.topic_update_counter import TopicUpdateCounter, LocalTopicUpdateCounter
from main.service.topic_modeling.preprocessing.BERT_preprocessor import BERTPreprocessor
from main.service.topic_modeling.topic_model import TopicModel

from main.utils.base_logger import get_logger
from main.service.topic_modeling.training.training_set_provider import TrainingSetProvider


class BERTopicModel(TopicModel):

    def __init__(self,
                 topic_update_counter: TopicUpdateCounter = LocalTopicUpdateCounter(),
                 model_loader: LocalModelLoader = BERLocalModelLoader(),
                 logger=get_logger("BERTopicModel")
                 ):
        super().__init__()
        self.topic_update_counter = topic_update_counter
        self.pre_processor = BERTPreprocessor()
        self.model_loader = model_loader
        self.topic_labels = []
        self.model = self._compose_model()
        self.logger = logger

    def _compose_model(self):
        # remove custom stop words after embeddings are created to guarantee full context
        vectorizer_model = OnlineCountVectorizer(stop_words=self.pre_processor.custom_stop_words())
        # use tfidf transformer to reduce the impact of frequent words
        ctfidf_model = ClassTfidfTransformer(reduce_frequent_words=True)
        #umap_model = UMAP(n_neighbors=15, n_components=5, min_dist=0.0, metric='cosine', random_state=42)
        cluster_model = River(bertopic_hyperparams["cluster_model"])
        # diversify words to describe topics
        self.representation_model = TopicLabelingFactory().compose_labeling(TopicLabelingType.MAX_MARGINAL)
        return BERTopic(bertopic_hyperparams["language"],
                        n_gram_range=(1, bertopic_hyperparams["n_grams_max"]),
                        vectorizer_model=vectorizer_model,
                        ctfidf_model=ctfidf_model,
                        representation_model=self.representation_model,
                        #umap_model=umap_model,
                        hdbscan_model=cluster_model
                        )

    def label_and_learn(self, posts: [[Post]], skip_learn: bool) -> [Post]:
        # train batched using a tmp model
        # assign topic id to post
        if not skip_learn:
            post_content = [post.content for post_list in posts for post in post_list]
            self.model.partial_fit(post_content)
            self.store_model()
        for post_list in posts:
            for post in post_list:
                post.topic = self.get_topic(post)
        return posts

    def get_topic(self, post: Post) -> int:
        dist, _ = self.model.approximate_distribution(documents=[post.content])
        topic_index = np.argmax(dist)
        self.logger.debug("index of topic: {}".format(topic_index))
        return topic_index

    def compose_topics(self) -> [Topic]:
        # compose topics
        # include id
        # keywords
        # label
        topics = []
        topic_labels = self._compose_topic_labels()
        topics_raw = self.model.get_topic_info()
        last_topic_sent = self.topic_update_counter.get_topic_count()
        new_topics = topics_raw[topics_raw["Topic"] > last_topic_sent]
        for _, topic in new_topics.iterrows():
            if topic["Topic"] != -1:
                topics.append(Topic(
                    topic_id=topic["Topic"],
                    label=topic_labels[topic["Topic"]],
                    keywords=topic["Representation"][:5],
                    child_ids=[],
                    parent_id=-1,
                ))
        self.topic_update_counter.update_topic_count(topics_raw["Topic"].max())
        return topics

    def load_model(self, force_train=False) -> bool:
        self.logger.info("load model")
        self.model = self.model_loader.load_model(bertopic_hyperparams["model_name"])
        return self.model is not None

    def train_model(self, posts: [Post] = None):
        self.model = self._compose_model()
        self.topic_update_counter.update_topic_count(-1)
        if posts is None:
            self.logger.info("Start data preprocessing")
            training_set = TrainingSetProvider().load_data_set()
            processed_data = self.pre_processor.preprocess_data(training_set)
            self.logger.info("Start model training")
            self.model.partial_fit(processed_data)
            self.logger.info("Finished model training")
            self.logger.info("Number of topics: {}".format(len(self.model.get_topics())))
        else:
            pass

    def store_model(self):
        self.model_loader.store_model(self.model, bertopic_hyperparams["model_name"])

    def _compose_topic_labels(self):
        self.logger.debug(f"compose topic labels")
        topic_representations = self.representation_model.extract_topics(self.model, None, None, self.model.get_topics())
        labels = {}
        for id, list in topic_representations.items():
            max_word, _ = max(list, key=lambda x: x[1])
            labels[id] = max_word
        return labels

    def create_dashboard_sub_pages(self):
        #hierarchical_topics = self.model.hierarchical_topics([])
        #fig = self.model.visualize_hierarchy(hierarchical_topics=hierarchical_topics)
        #fig.write_html("hierarchy.html")
        fig = self.model.visualize_term_rank()
        fig.write_html("term_rank.html")
        fig = self.model.visualize_heatmap()
        fig.write_html("heatmap.html")
        fig = self.model.visualize_topics()
        fig.write_html("topics.html")
        fig = self.model.visualize_barchart()
        fig.write_html("barchart.html")