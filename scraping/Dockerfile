FROM python:3.12-slim

WORKDIR /app

COPY . /app

# Overwrite localhost config for container communication
COPY ./resources/config.docker.ini /app/resources/config.ini

COPY ./resources/mastodon/mastodon_instances.docker.json /app/resources/mastodon/mastodon_instances.json

RUN pip install --no-cache-dir -r requirements.txt

RUN apt-get update && apt-get -y install cron curl dos2unix

COPY scraper-cronjob /etc/cron.d/scraper-cronjob

RUN dos2unix /etc/cron.d/scraper-cronjob

RUN chmod 0644 /etc/cron.d/scraper-cronjob

RUN crontab /etc/cron.d/scraper-cronjob

RUN touch /var/log/cron.log

EXPOSE 5003

COPY entrypoint.sh /entrypoint.sh

RUN dos2unix /entrypoint.sh

RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]