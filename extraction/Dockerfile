FROM python:3.12-slim

WORKDIR /app

COPY . /app

COPY ./resources/config.docker.ini /app/resources/config.ini

RUN pip install --no-cache-dir -r requirements.txt

EXPOSE 5001

CMD [ "python3", "-m" , "flask", "run", "--host=0.0.0.0", "--port=5001"]