FROM ubuntu:22.04

ARG NODE_MAJOR=20
ARG RAILS_ENV=development

ENV DB_HOST=db
ENV DB_USER=verifeed
ENV DB_NAME=mastodon_development
ENV DB_PORT=5433

ENV REMOTE_DEV=true
ENV RAILS_ENV=development

EXPOSE 80

RUN apt-get update && apt-get install -y curl wget gnupg apt-transport-https lsb-release ca-certificates

RUN mkdir -p /etc/apt/keyrings && \
    curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key | gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg && \
    echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_${NODE_MAJOR}.x nodistro main" | tee /etc/apt/sources.list.d/nodesource.list && \
    apt-get update && \
    apt-get install nodejs -y

RUN DEBIAN_FRONTEND=noninteractive TZ=Europe/Vienna apt-get -y install tzdata

RUN apt-get install -y \
    imagemagick ffmpeg libpq-dev libxml2-dev libxslt1-dev file git-core \
    g++ libprotobuf-dev protobuf-compiler pkg-config nodejs gcc autoconf \
    bison build-essential libssl-dev libyaml-dev libreadline6-dev \
    zlib1g-dev libncurses5-dev libffi-dev libgdbm-dev \
    nginx redis-server redis-tools postgresql postgresql-contrib \
    certbot python3-certbot-nginx libidn11-dev libicu-dev libjemalloc-dev \
    ruby ruby-dev

RUN corepack enable && \
    yarn set version classic

RUN gem install bundler --no-document

RUN git clone https://github.com/mastodon/mastodon.git

WORKDIR mastodon

RUN git checkout stable-4.2

COPY .env .
COPY start.sh .
COPY Procfile.dev .
COPY config.hosts .

RUN sed -i "s/limit: 300/limit: 99999/g" config/initializers/rack_attack.rb
RUN sed -i "s/limit: 300/limit: 99999/g" config/initializers/rack_attack.rb
RUN sed -i "s/limit: 1_500/limit: 99999/g" config/initializers/rack_attack.rb
RUN sed -i "s/limit: 1500/limit: 99999/g" config/initializers/rack_attack.rb
RUN sed -i "s/limit: 30/limit: 99999/g" config/initializers/rack_attack.rb
RUN sed -i "s/limit: 5/limit: 99999/g" config/initializers/rack_attack.rb
RUN sed -i "s/limit: 25/limit: 99999/g" config/initializers/rack_attack.rb

RUN bundle install

RUN yarn install

RUN gem install foreman --no-document

RUN sed -i 's/\r$//' /mastodon/start.sh
RUN chmod +x /mastodon/start.sh

COPY --from=forlabs/libfaketime /faketime.so /faketime.so

ENV LD_PRELOAD=/faketime.so 
ENV FAKETIME="@2023-11-24 20:00:00"

ENTRYPOINT ["/mastodon/start.sh"]