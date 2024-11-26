#!/bin/bash

service cron start

export FLASK_APP=app.py
flask run --host=0.0.0.0 --port=5003

tail -f /var/log/cron.log
