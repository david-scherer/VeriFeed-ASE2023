from dataclasses import dataclass
from enum import Enum


class SourceType(Enum):
    MASTODON = "mastodon"
    TWITTER = "twitter"
    FACEBOOK = "facebook"
    REDDIT = "reddit"
    UNKNOWN = "unknown"


@dataclass
class Source:
    url: str
    source_type: SourceType

    def to_dict(self):
        return {"url": self.url, "source_type": self.source_type.value}
