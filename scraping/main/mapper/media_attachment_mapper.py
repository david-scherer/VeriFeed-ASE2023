from main.models.media_attachment import MediaAttachment, MediaType


class MediaAttachmentMapper:
    @staticmethod
    def map_mastodon_media_to_dto(mastodon_media):
        media_type = mastodon_media.get("type").lower()
        values = [member.value for member in MediaType]
        if media_type not in values:
            media_type = MediaType.UNKNOWN.value

        return MediaAttachment(
            media_attachment_id=mastodon_media["id"],
            type=MediaType(media_type),
            url=mastodon_media["url"],
        )

    @staticmethod
    def map_reddit_media_to_dto(reddit_media):
        media_type = reddit_media.get("e")

        if media_type is None:
            media_type = MediaType.UNKNOWN.value
        elif media_type == "Image":
            media_type = MediaType.IMAGE.value
        elif media_type == "RedditVideo":
            media_type = MediaType.VIDEO.value
        elif media_type == "AnimatedImage":
            media_type = MediaType.GIF.value
        else:
            media_type = MediaType.UNKNOWN.value

        return MediaAttachment(
            media_attachment_id=reddit_media["id"],
            type=MediaType(media_type),
            url=reddit_media["s"]["u"],
        )
