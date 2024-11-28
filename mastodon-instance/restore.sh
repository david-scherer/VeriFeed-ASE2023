#!/bin/bash

set -e

pg_restore -Fc --username "$POSTGRES_USER" -n public --no-owner --role=verifeed -d mastodon_development /docker-entrypoint-initdb.d/backup.dump