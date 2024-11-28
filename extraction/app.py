from flask import Flask
from main.controller.extraction_controller import extraction_controller_bp

app = Flask(__name__)
app.register_blueprint(extraction_controller_bp)


if __name__ == "__main__":
    app.run(port=5001)
