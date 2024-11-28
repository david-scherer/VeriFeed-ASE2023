from datetime import datetime

from bs4 import BeautifulSoup

from main.mapper.account_mapper import AccountMapper
from main.mapper.hashtag_mapper import HashtagMapper
from main.mapper.media_attachment_mapper import MediaAttachmentMapper
from main.mapper.parent_post_mapper import ParentPostMapper
from main.models.hashtag import Hashtag
from main.models.post import Post
from main.models.post_metrics import PostMetrics


class PostMapper:
    """
    Mapper class for mapping a post from a social media platform to a DTO.
    Every single post has the additional field called 'additional_scraping_fields'
    which can contain additional information about the single post.
    """

    @staticmethod
    def map_mastodon_post_to_dto(mastodon_post):
        return Post(
            post_id=mastodon_post["id"],
            url=mastodon_post["url"],
            created_at=datetime.fromisoformat(mastodon_post["created_at"]),
            account=AccountMapper.map_mastodon_account_to_dto(mastodon_post["account"]),
            content=BeautifulSoup(mastodon_post["content"], "html.parser").get_text(),
            metrics=PostMetrics(
                reposts_count=mastodon_post["reblogs_count"],
                favourites_count=mastodon_post["favourites_count"],
                replies_count=mastodon_post["replies_count"],
            ),
            parent_post=None
            if (
                additional_scraping_fields := mastodon_post.get(
                    "additional_scraping_fields"
                )
            )
            is None
            or additional_scraping_fields.get("parent_post") is None
            else ParentPostMapper.map_mastodon_parent_post_to_dto(
                additional_scraping_fields.get("parent_post")
            ),
            hashtags=[
                HashtagMapper.map_mastodon_hashtag_to_dto(tag)
                for tag in mastodon_post["tags"]
            ],
            media_attachments=[
                MediaAttachmentMapper.map_mastodon_media_to_dto(media)
                for media in mastodon_post["media_attachments"]
            ],
            edited=not (mastodon_post["edited_at"] is None),
        )

    @staticmethod
    def map_reddit_post_to_dto(reddit_post):
        return Post(
            post_id=reddit_post["id"],
            url="https://www.reddit.com" + reddit_post["permalink"],
            created_at=datetime.fromtimestamp(reddit_post["created_utc"]),
            account=AccountMapper.map_reddit_account_to_dto(reddit_post["author"]),
            content=reddit_post["title"]
            + ("\n\n" + reddit_post["selftext"] if reddit_post["selftext"] else "")
            + ("\n\n" + reddit_post["url"] if not reddit_post["selftext"] else ""),
            metrics=PostMetrics(
                reposts_count=reddit_post["num_crossposts"],
                favourites_count=reddit_post["score"],
                replies_count=reddit_post["num_comments"],
            ),
            parent_post=None,
            hashtags=[
                Hashtag(
                    name=reddit_post["link_flair_text"],
                    url="https://www.reddit.com/"
                    + reddit_post["subreddit_name_prefixed"]
                    + '?f=flair_name:"'
                    + reddit_post["link_flair_text"]
                    + '"',
                )
            ]
            if reddit_post["link_flair_text"]
            else [],
            media_attachments=[
                MediaAttachmentMapper.map_reddit_media_to_dto(media)
                for media in reddit_post["media_metadata"].values()
            ],
            edited=reddit_post["edited"],
        )
