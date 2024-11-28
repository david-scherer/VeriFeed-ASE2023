from datasets import load_dataset, concatenate_datasets

from main.utils.base_logger import get_logger

logger = get_logger("training_set_loader")


class TrainingSetProvider:
    def load_data_set(self) -> [str]:
        logger.info("Download training set")
        # load datasets
        # tweets labeled with democrats/republicans
        political_tweets = load_dataset("Jacobvs/PoliticalTweets", split="train")
        #political_comments = load_dataset("jbochenek/political", split="train")
        #twitter_financial_news = load_dataset("zeroshot/twitter-financial-news-topic", split="train")
        #political_justifications = load_dataset("od21wk/political_news_justifications", split="train")
        #real_and_fake_news = load_dataset("GonzaloA/fake_news", split="train")
        # already tokenized
        #covid_vaccine_attitudes = load_dataset("webimmunization/COVID-19-vaccine-attitude-tweets")
        #covid_vaccine_conspiracy = load_dataset("webimmunization/COVID-19-conspiracy-theories-tweets")
        logger.info("Finished downloading")
        posts = concatenate_datasets(
            [
                political_tweets,
                #political_comments,
                #twitter_financial_news,
                #political_justifications,
                #real_and_fake_news,
                #covid_vaccine_attitudes,
                #covid_vaccine_conspiracy,
            ]
        ).to_dict()["text"]
        return [sample for sample in posts if sample is not None]

