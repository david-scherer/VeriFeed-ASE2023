from main.models.parent_post import ParentPost


class ParentPostMapper:
    @staticmethod
    def map_mastodon_parent_post_to_dto(mastodon_parent_post):
        return ParentPost(
            parent_post_id=mastodon_parent_post["parent_post_id"],
            parent_post_url=mastodon_parent_post["parent_post_url"],
        )

    @staticmethod
    def map_reddit_parent_post_to_dto(reddit_parent_post):
        pass
