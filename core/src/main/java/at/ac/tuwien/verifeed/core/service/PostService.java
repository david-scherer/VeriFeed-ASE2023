package at.ac.tuwien.verifeed.core.service;

import at.ac.tuwien.verifeed.core.dto.CommentaryDto;
import at.ac.tuwien.verifeed.core.dto.extraction.UngroupedPostCollectionDto;
import at.ac.tuwien.verifeed.core.dto.recognition.TopicsAndPostCollectionDto;
import at.ac.tuwien.verifeed.core.entities.Commentary;
import at.ac.tuwien.verifeed.core.entities.Posting;
import at.ac.tuwien.verifeed.core.entities.Source;

import java.util.List;

public interface PostService {
    void handleUngroupedPostCollection(UngroupedPostCollectionDto ungroupedPostCollectionDto);

    void handleGroupedPostCollection(TopicsAndPostCollectionDto topicsAndPostCollection);

    Commentary addCommentToPost(String id, CommentaryDto commentaryDto);

    Posting getPost(String id);

    Commentary getComment(String commentId);

    List<Commentary> getComments(String id);

    void deleteComment(String commentId);

    void upvote(String commentId);

    void downvote(String commentId);

    void unvote(String commentId);

    void editCommentary(String commentId, CommentaryDto commentaryDto);

    List<Posting> getPosts(Long start, Long numberOfPosts, Boolean onlyCommented, String q);

    void blockPost(String id);

    void addSourceToBlacklist(Source source);
}
