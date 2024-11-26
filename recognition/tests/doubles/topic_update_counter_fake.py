from main.service.topic_modeling.loading.topic_update_counter import TopicUpdateCounter


class LocalTopicUpdateCounterFake(TopicUpdateCounter):

    def __init__(self):
        self.last_topic_stored = -1

    def get_topic_count(self):
        return self.last_topic_stored


    def update_topic_count(self, new_value: int):
        self.last_topic_stored = new_value
