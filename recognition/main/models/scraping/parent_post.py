from dataclasses import dataclass

from dataclass_wizard import JSONWizard


@dataclass
class ParentPost(JSONWizard):
    parent_post_id: str
    parent_post_url: str

    def to_dict(self):
        return {
            "parent_post_id": self.parent_post_id,
            "parent_post_url": self.parent_post_url,
        }
