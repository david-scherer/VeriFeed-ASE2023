from enum import Enum


class TopicLabelingType(Enum):
    MAX_MARGINAL = 1
    COHERE = 2
    OPENAI = 3
