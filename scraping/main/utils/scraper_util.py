import requests
from requests import JSONDecodeError, Response

from main.models.errors.http_request_error import HttpRequestError
from main.models.errors.scrape_status_error import ScrapeStatusError
from main.models.source import SourceType


def make_http_request(
        headers: dict, method: str, url: str, params: dict = None, json_data: dict = None
) -> Response | HttpRequestError:
    """
    Make HTTP request to the given URL with the given method and parameters
    :param headers: Headers to be passed in the HTTP request
    :param method: HTTP method
    :param url: URL to make HTTP request to
    :param params: Parameters to be passed in the HTTP request
    :param json_data: JSON data to be passed in the HTTP request
    :return: Response object of the HTTP request
    """

    response = None
    try:
        response = requests.request(
            method,
            url,
            headers=headers,
            json=json_data,
            params=params,
            timeout=300,
        )
        response.raise_for_status()
        return response
    except requests.RequestException as e:
        raise HttpRequestError({e}, response) from e


def scrape_single_status(headers: dict, status_url: str) -> dict | ScrapeStatusError:
    """
    Scrape single status details as json from the given URL
    :param headers: Headers to be passed in the HTTP request
    :param status_url: URL to make HTTP request to (json response)
    :return: JSON object of the response
    """

    try:
        response = make_http_request(
            headers=headers,
            method="GET",
            url=status_url,
        )
        return response.json()
    except HttpRequestError as e:
        raise ScrapeStatusError(
            f"Failed to scrape single status details " f"from the URL '{status_url}'!"
        ) from e
    except JSONDecodeError as e:
        raise ScrapeStatusError("Response is not a valid json!") from e


def identify_social_media_platform(url: str) -> SourceType:
    """
    Identifies the social media platform of the given URL
    :param url: URL to be identified
    :return: SourceType of the given URL
    """

    try:
        headers = requests.head(url, timeout=10).headers
        server_value = headers.get("Server")

        if ((server_value and server_value.lower() == "mastodon")
                or ("https://mastodon" in url)
                or (url.startswith("https://") and "mastodon.com" in url)):
            return SourceType.MASTODON
        if "reddit.com" in url:
            return SourceType.REDDIT
        if "twitter.com" in url or "x.com" in url:
            return SourceType.TWITTER
        if "facebook.com" in url:
            return SourceType.FACEBOOK
        return SourceType.UNKNOWN
    except requests.RequestException:
        return SourceType.UNKNOWN
