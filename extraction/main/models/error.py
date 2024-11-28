from dataclasses import dataclass

from dataclass_wizard import JSONWizard


@dataclass
class Error(JSONWizard):
    message: str

    def __init__(self, message: str):
        self.message = message
