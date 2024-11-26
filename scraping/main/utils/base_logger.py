import logging
import os
import sys

from main.models.errors.config_reader_error import ConfigReaderError
from main.utils.config_reader import ConfigReader

DEFAULT_LOG_DIR = "logs"
DEFAULT_LOG_LEVEL = "DEBUG"

try:
    config_reader = ConfigReader()
    LOG_DIR = config_reader.read_config("GENERAL", "LOG_DIR")
    LOG_LEVEL = config_reader.read_config("GENERAL", "LOG_LEVEL")
except ConfigReaderError as e:
    print(f"Error: {e}")
    LOG_DIR = DEFAULT_LOG_DIR
    LOG_LEVEL = DEFAULT_LOG_LEVEL

if not os.path.exists(LOG_DIR):
    os.makedirs(LOG_DIR)


def get_logger(module_name):
    logger_instance = logging.getLogger(module_name)

    file_handler = logging.FileHandler(f"{LOG_DIR}/{module_name}.log")
    stdout_handler = logging.StreamHandler(stream=sys.stdout)

    formatter = logging.Formatter(
        "%(asctime)s - %(module)s - %(levelname)s - [%(filename)s:%(lineno)d] - %(message)s"
    )
    file_handler.setFormatter(formatter)
    stdout_handler.setFormatter(formatter)

    logger_instance.setLevel(LOG_LEVEL)
    logger_instance.addHandler(file_handler)
    logger_instance.addHandler(stdout_handler)

    return logger_instance
