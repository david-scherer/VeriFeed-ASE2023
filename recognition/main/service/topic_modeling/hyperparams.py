from river import cluster

bertopic_hyperparams = {
    "max_marginal_diversity": 0.3,
    "n_grams_max": 5,
    "language": "english",
    "top_n_words": 10,
    "nr_topics": None,
    "model_name": "BERTopic_model",
    "topic_label_num_of_words": 3,
    "min_similarity": 0.7,
    "cluster_model": cluster.DBSTREAM(
        # 1 0.0001 0.8 1.0
        clustering_threshold=1.0,
        fading_factor=0.0001,
        intersection_factor=0.8,
        minimum_weight=1.0
    )
}
