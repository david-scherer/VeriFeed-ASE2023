from typing import List
from scipy.cluster import hierarchy
from sentence_transformers import SentenceTransformer

from main.utils.base_logger import get_logger
from main.models.scraping.post import Post

logger = get_logger("similarity_service")


class SimilarityService:
    def group_posts(self, posts: List[Post], threshold: float) -> List[List[Post]]:
        # Clustering with only 1 elements causes an error, so we "cheat" a bit
        # in that case.
        if len(posts) == 1:
            return [[posts[0]]]

        """
        Calculate groups of posts that are similar to a degree given by the
        threshold variable.

        :param post_collection: Structure containing information about a list
                                of posts and the posts themselves.
        :param threshold: Threshold of similarity
        """

        if posts is None or len(posts) == 0:
            logger.debug("Posts list was empty")
            return []

        # Get cleaned texts (without hashtags and handles)
        post_texts = [
            self.__clean_post_text(post.content) for post in posts
        ]

        logger.debug(
            "{} input texts:\n{}".format(
                len(post_texts),
                "\n".join(["\t{}".format(post_text) for post_text in post_texts]),
            )
        )

        # Generate embeddings (numerical representations of text)
        model = SentenceTransformer("sentence-transformers/all-mpnet-base-v2")
        embeddings = model.encode(post_texts)

        # Create the clusters.
        # We use the 'average' distance between the elements of cluster a and
        # the elements cluster b.
        # The distance itself is calculated using  cosine similarity.
        clusters = hierarchy.linkage(embeddings, "average", metric="cosine")

        # Create flat clusters, resulting in a list of integers where each
        # element i is the flat cluster number of the cluster the original
        # element i belongs to.
        flat_clusters = hierarchy.fcluster(clusters, threshold, criterion="distance")

        num_of_clusters = len(set(flat_clusters))
        logger.debug(f"Created {num_of_clusters} clusters")

        # Create groups array with size of number of unique clusters
        groups: list[list[Post]] = [[] for _ in range(num_of_clusters)]

        # Combine the flat cluster numbers with the original posts.
        for post, flat_cluster_num in zip(posts, flat_clusters):
            groups[flat_cluster_num - 1].append(post)

        return groups

    @staticmethod
    def __clean_post_text(post: str) -> str:
        return " ".join(
            [
                word
                for word in post.split(" ")
                if not (word.startswith("#") or word.startswith("@"))
            ]
        )
