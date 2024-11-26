from dataclasses import dataclass


@dataclass
class ParentPost:
    parent_post_id: str
    parent_post_url: str

    def to_dict(self):
        return {
            "parent_post_id": self.parent_post_id,
            "parent_post_url": self.parent_post_url,
        }
