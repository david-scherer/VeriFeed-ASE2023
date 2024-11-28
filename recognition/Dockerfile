FROM python:3.11-slim

WORKDIR /app

COPY . /app

COPY ./resources/config.docker.ini /app/resources/config.ini

RUN apt-get update && apt-get install -y gcc

RUN pip install --no-cache-dir -r requirements.txt

RUN python -c "import nltk; nltk.download('stopwords')"

EXPOSE 5002

CMD [ "python3", "-m" , "flask", "run", "--host=0.0.0.0", "--port=5002"]