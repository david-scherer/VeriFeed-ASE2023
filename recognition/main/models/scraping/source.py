from dataclasses import dataclass
from enum import Enum

from dataclass_wizard import JSONWizard


class SourceType(Enum):
    MASTODON = "mastodon"
    TWITTER = "twitter"
    FACEBOOK = "facebook"
    REDDIT = "reddit"
    UNKNOWN = "unknown"


@dataclass
class Source(JSONWizard):
    url: str
    source_type: SourceType

    def to_dict(self):
        return {"url": self.url, "source_type": self.source_type.value}
