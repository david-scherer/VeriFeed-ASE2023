import cohere
import os

from bertopic.representation import Cohere
from bertopic.representation import LangChain, MaximalMarginalRelevance
from langchain.chains.question_answering import load_qa_chain
from langchain_community.llms.openai import OpenAI
from main.service.topic_modeling.hyperparams import bertopic_hyperparams
from main.service.topic_modeling.labeling.TopicLabelingType import TopicLabelingType
from main.utils.base_logger import get_logger

logger = get_logger("BERTopicModel")
openai_api_key_name = 'OPENAI_key'
cohere_api_key_name = 'COHERE_key'
openAI_prompt = prompt = "What are these posts about? Please give a single label."


class TopicLabelingFactory:

    def compose_labeling(self, type: TopicLabelingType):
        match type:
            case TopicLabelingType.COHERE:
                if cohere_api_key_name in os.environ:
                    api_key = os.environ[cohere_api_key_name]
                    co = cohere.Client(api_key)
                    return Cohere(co)
                else:
                    logger.warn("could not find Cohere api key, fall back to max marginal representation")
                    return self.compose_labeling(TopicLabelingType.MAX_MARGINAL)
            case TopicLabelingType.OPENAI:
                if openai_api_key_name in os.environ:
                    api_key = os.environ[openai_api_key_name]
                    openai = OpenAI(temperature=0, openai_api_key=api_key)
                    chain = load_qa_chain(openai, chain_type="stuff")
                    return LangChain(chain, prompt=prompt)
                else:
                    logger.warn("could not find OpenAI api key, fall back to max marginal representation")
                    return self.compose_labeling(TopicLabelingType.MAX_MARGINAL)
            case TopicLabelingType.MAX_MARGINAL:
                return MaximalMarginalRelevance(diversity=bertopic_hyperparams["max_marginal_diversity"])
