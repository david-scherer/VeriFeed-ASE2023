from dataclasses import dataclass


@dataclass
class Hashtag:
    name: str
    url: str

    def to_dict(self):
        return {"name": self.name, "url": self.url}
