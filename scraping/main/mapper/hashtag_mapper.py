from main.models.hashtag import Hashtag


class HashtagMapper:
    @staticmethod
    def map_mastodon_hashtag_to_dto(mastodon_hashtag):
        return Hashtag(name=mastodon_hashtag["name"], url=mastodon_hashtag["url"])

    @staticmethod
    def map_reddit_hashtag_to_dto(reddit_hashtag):
        pass
