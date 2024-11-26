from dataclasses import dataclass
from datetime import datetime
from typing import List, Optional

from dataclass_wizard import JSONWizard

from main.models.scraping.account import Account
from main.models.scraping.hashtag import Hashtag
from main.models.scraping.media_attachment import MediaAttachment
from main.models.scraping.parent_post import ParentPost
from main.models.scraping.post_metrics import PostMetrics


# pylint: disable=too-many-instance-attributes
@dataclass
class Post(JSONWizard):
    post_id: str
    url: str
    created_at: datetime
    account: Account
    content: str
    metrics: PostMetrics
    parent_post: Optional[ParentPost]
    hashtags: List[Hashtag]
    media_attachments: List[MediaAttachment]
    edited: bool = False
    topic: int = -1

    internal_post_id: str = None

    def to_dict(self):
        return {
            "post_id": self.post_id,
            "parent_post": None if self.parent_post is None else self.parent_post.to_dict(),
            "url": self.url,
            "created_at": self.created_at.isoformat(),
            "account": self.account.to_dict(),
            "hashtags": [hashtag.to_dict() for hashtag in self.hashtags],
            "content": self.content,
            "edited": self.edited,
            "media_attachments": [
                media_attachment.to_dict()
                for media_attachment in self.media_attachments
            ],
            "metrics": self.metrics.to_dict(),
            "topic": int(self.topic),
            "internal_post_id": self.internal_post_id,
        }
