from dataclasses import dataclass

from dataclass_wizard import JSONWizard


@dataclass
class Hashtag(JSONWizard):
    name: str
    url: str

    def to_dict(self):
        return {"name": self.name, "url": self.url}
