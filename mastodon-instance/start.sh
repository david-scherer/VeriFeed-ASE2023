#!/bin/sh

RAILS_ENV=development ./bin/tootctl accounts refresh --all
RAILS_ENV=development ./bin/tootctl feeds build

foreman start