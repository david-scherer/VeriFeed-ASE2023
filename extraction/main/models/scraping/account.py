from dataclasses import dataclass
from datetime import datetime

from dataclass_wizard import JSONWizard


@dataclass
class Account(JSONWizard):
    account_id: str
    username: str
    created_at: datetime

    def to_dict(self):
        return {
            "account_id": self.account_id,
            "username": self.username,
            "created_at": self.created_at.isoformat(),
        }
