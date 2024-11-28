from main.utils.base_logger import get_logger
import os

logger = get_logger("topic_update_counter")
topic_count_file = 'topic_count.txt'
dir_path = os.path.dirname(os.path.realpath(__file__))
resources_location = os.path.join(dir_path, "..", "..", "..", "..", "resources")


class TopicUpdateCounter:

    def get_topic_count(self):
        pass

    def update_topic_count(self, new_value: int):
        pass


class LocalTopicUpdateCounter(TopicUpdateCounter):
    def get_topic_count(self):
        try:
            full_file_name = os.path.join(resources_location, topic_count_file)
            with open(full_file_name, 'r') as file:
                topic_count = int(file.read())
                logger.info(f"Read topic count: {topic_count}")
                return topic_count
        except FileNotFoundError:
            logger.info("initial topic count load")
            return -1

    def update_topic_count(self, new_value: int):
        full_file_name = os.path.join(resources_location, topic_count_file)
        with open(full_file_name, 'w') as file:
            logger.info(f"Store new topic count: {new_value}")
            file.write(str(new_value))
