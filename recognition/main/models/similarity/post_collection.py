from dataclasses import dataclass, field
from datetime import datetime
from typing import List
from uuid import uuid4

from dataclass_wizard import JSONWizard

from main.models.scraping.source import Source
from .post import Post


@dataclass
class PostCollection(JSONWizard):
    """
    Post collection used as output for the similarity service.
    Compared to the post collection received from the extraction service,
    posts are already grouped by similarity s.t. posts that are considered
    equivalent appear in the same sub-list.
    """

    trigger_timestamp: datetime
    post_collection_id: str = field(default_factory=lambda: str(uuid4()))
    # List contains "groups" of posts that are considered equivalent.
    posts: List[List[Post]] = field(default_factory=list)
    source: Source = field(default_factory=Source)
    created_at: datetime = field(default_factory=datetime.now)

    def to_dict(self):
        return {
            "post_collection_id": self.post_collection_id,
            "posts": [[post.to_dict() for post in group] for group in self.posts],
            "source": self.source.to_dict(),
            "created_at": self.created_at.isoformat(),
            "trigger_timestamp": self.trigger_timestamp.isoformat(),
        }
