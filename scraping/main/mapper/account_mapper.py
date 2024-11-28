from datetime import datetime

from main.models.account import Account


class AccountMapper:
    @staticmethod
    def map_mastodon_account_to_dto(mastodon_account):
        return Account(
            account_id=mastodon_account["id"],
            username=mastodon_account["username"],
            created_at=datetime.fromisoformat(mastodon_account["created_at"]),
        )

    @staticmethod
    def map_reddit_account_to_dto(reddit_account):
        return Account(
            account_id=reddit_account["id"],
            username=reddit_account["name"],
            created_at=datetime.fromtimestamp(reddit_account["created_utc"]),
        )
