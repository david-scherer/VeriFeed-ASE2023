# Scraping Service

- for Mastodon
- for Reddit


## Initializing the virtual env

- Run `init_venv.sh`
- Activate the environment if you're not yet in it via `source venv/bin/activate`


## Running the service

`python app.py`


## Config

The config parameters are located in `resourcs/config.ini`.


## Extraction Service

The scraping service is sending the scraped data to the extraction service.
The extraction service url/endpoint is defined in the config file `resources/config.ini`.


## Cronjob

In the file `scraper-cronjob` the cronjob for the scraping service is defined.
This file HAS TO BE formatted with Unix line endings (LF) and not Windows line endings (CRLF) or otherwise the cronjob won't work.
Currently, the cronjob is set to run 10 minutes.


## Reddit

### Reddit Instances (Subreddits)

To iterate over all given reddit instances (subreddits) fill in the subreddits in the json file `resources/reddit/reddit_instances.json`.

### Reddit User/Scripts

In the config file `resources/config.ini` the 'CLIENT_ID', 'CLIENT_SECRET' and 'USER_AGENT' have to be filled in. 
These values are needed for python 'praw' to access the reddit api.


## Mastodon

### Mastodon Instances

To iterate over all given mastodon instances fill in the instance in the json file `resources/mastodon/mastodon_instances.json`.

