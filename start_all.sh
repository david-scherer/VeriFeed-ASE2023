#!/bin/bash

docker network create mastodon-instance_mastonet

docker-compose -f mastodon-instance/docker-compose.yml up -d
docker-compose up -d
