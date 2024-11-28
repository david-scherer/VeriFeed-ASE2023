from flask import Flask

from main.controller.recognition import recognition_controller_bp

app = Flask(__name__)
app.register_blueprint(recognition_controller_bp)


if __name__ == "__main__":
    app.run(port=5002, debug=True)
