from dataclasses import dataclass


@dataclass
class PostMetrics:
    reposts_count: int
    favourites_count: int
    replies_count: int

    def to_dict(self):
        return {
            "reposts_count": self.reposts_count,
            "favourites_count": self.favourites_count,
            "replies_count": self.replies_count,
        }
