from dataclasses import dataclass
from datetime import datetime

from dataclass_wizard import JSONWizard


# pylint: disable=too-many-instance-attributes
@dataclass
class Topic(JSONWizard):
    topic_id: int
    label: str
    keywords: [str]
    parent_id: int
    child_ids: [int]

    def to_dict(self):
        return {
            "topic_id": int(self.topic_id),
            "label": self.label,
            "keywords": self.keywords,
            "parent_id": self.parent_id,
            "child_ids": self.child_ids
        }
