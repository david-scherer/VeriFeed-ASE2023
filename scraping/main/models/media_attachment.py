from dataclasses import dataclass
from enum import Enum


class MediaType(Enum):
    IMAGE = "image"
    VIDEO = "video"
    GIF = "gif"
    GIFV = "gifv"
    UNKNOWN = "unknown"


@dataclass
class MediaAttachment:
    media_attachment_id: str
    type: MediaType
    url: str

    def to_dict(self):
        return {
            "media_attachment_id": self.media_attachment_id,
            "type": self.type.value,
            "url": self.url,
        }
