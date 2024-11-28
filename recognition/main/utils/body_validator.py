from main.models.error import Error


def validate_body(body, keys: list[str]) -> Error | None:
    """
    Ensures that all required keys are present in the body, otherwise returns
    an error.

    :param body: The body containing the data
    :param keys: The list of keys that should exist in the body
    """
    for key in keys:
        if key not in body:
            return Error(f"Missing required field {key}")
    return None
