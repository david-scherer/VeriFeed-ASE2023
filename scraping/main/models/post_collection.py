from dataclasses import dataclass, field
from datetime import datetime
from typing import List
from uuid import uuid4

from main.models.post import Post
from main.models.source import Source


@dataclass
class PostCollection:
    trigger_timestamp: datetime
    post_collection_id: str = field(default_factory=lambda: str(uuid4()))
    posts: List[Post] = field(default_factory=list)
    source: Source = field(default_factory=Source)
    created_at: datetime = field(default_factory=datetime.now)

    def to_dict(self):
        return {
            "post_collection_id": self.post_collection_id,
            "posts": [post.to_dict() for post in self.posts],
            "source": self.source.to_dict(),
            "created_at": self.created_at.isoformat(),
            "trigger_timestamp": self.trigger_timestamp.isoformat(),
        }
