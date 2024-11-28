from flask import Flask

from main.controller.base_scrape_controller import base_scrape_controller_bp
from main.controller.mastodon_controller import mastodon_controller_bp
from main.controller.reddit_controller import reddit_controller_bp

app = Flask(__name__)
app.register_blueprint(mastodon_controller_bp)
app.register_blueprint(reddit_controller_bp)
app.register_blueprint(base_scrape_controller_bp)

if __name__ == "__main__":
    app.run(port=5003, debug=True)
