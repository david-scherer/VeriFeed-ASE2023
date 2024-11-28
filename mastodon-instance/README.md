# Verifeed Mastodon Test Instance

This Mastodon test instance can be used to provide the functionality of a social network to test Verifeed services. By default, the instance is created from a database dump that already creates trending hashtags and posts. The data can be extended or replaced using the [dummy-data-generator](./dummy-data-generator/) as described in its README.

## Using the instance

Run `docker-compose up`. Building and running the instance from scratch can take a few minutes. After a few minutes you can access the frontend at localhost:3000.

## Access and credentials

- localhost:3000
- Username: admin@localhost
- Password: mastodonadmin