# Dummy data generator

## How to generate data in Mastodon instance

By default, the generator will insert data into the running Mastodon instance. You can add data in stages:

### Users

`python main.py users --users 150` with the `--users` flag being optional

### Posts

`python main.py posts --posts 150` with the `--posts` flag being optional

### Activity

`python main.py activity`

## Generate JSON

If you instead want to generate posts in a JSON format, simply run:

`python main.py posts --json`