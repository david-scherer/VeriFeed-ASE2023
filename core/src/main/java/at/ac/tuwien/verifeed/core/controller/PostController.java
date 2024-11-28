package at.ac.tuwien.verifeed.core.controller;

import at.ac.tuwien.verifeed.core.dto.CommentaryDto;
import at.ac.tuwien.verifeed.core.dto.PostingDto;
import at.ac.tuwien.verifeed.core.dto.extraction.UngroupedPostCollectionDto;
import at.ac.tuwien.verifeed.core.exception.EntityNotFoundException;
import at.ac.tuwien.verifeed.core.exception.ScrapingServiceException;
import at.ac.tuwien.verifeed.core.mapper.CommentaryMapper;
import at.ac.tuwien.verifeed.core.mapper.PostingMapper;
import at.ac.tuwien.verifeed.core.service.PostService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${web.api.base}/post")
public class PostController {
    private final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;

    private final CommentaryMapper commentaryMapper;

    private final PostingMapper postingMapper;

    @Autowired
    public PostController(PostService postService, CommentaryMapper commentaryMapper, PostingMapper postingMapper) {
        this.postService = postService;
        this.commentaryMapper = commentaryMapper;
        this.postingMapper = postingMapper;
    }

    @PostMapping("/collection/ungrouped")
    public ResponseEntity<Map<String, String>> handleUngroupedPostCollection(@RequestBody @Valid UngroupedPostCollectionDto ungroupedPostCollection) throws EntityNotFoundException {
        logger.info("Handle posts from ungrouped post collection received from extraction service");
        postService.handleUngroupedPostCollection(ungroupedPostCollection);

        logger.info("Posts from ungrouped post collection have been successfully handled.");

        Map<String, String> response = new HashMap<>();
        response.put("message", "Posts from ungrouped post collection have been successfully handled.");
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping()
    public ResponseEntity<List<PostingDto>> getPosts(
            @RequestParam Long offset,
            @RequestParam Long limit,
            @RequestParam Boolean onlyCommented,
            @RequestParam String q
    ) throws ScrapingServiceException, EntityNotFoundException {
        logger.info("Get posts with offset {}, limit {}, onlyCommented {}, q {}", offset, limit, onlyCommented, q);
        return ResponseEntity.status(200).body(postService.getPosts(offset, limit, onlyCommented, q).stream().map(postingMapper::entityToDto).toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void blockPost(@PathVariable("id") String id) {
        this.postService.blockPost(id);
    }

    @PreAuthorize("hasRole('JOURNALIST') and @userFilterBean.isVerified()")
    @PostMapping("/{id}/commentaries")
    public CommentaryDto addCommentToPost(@PathVariable("id") String id, @RequestBody @Valid CommentaryDto commentaryDto) {
        return commentaryMapper.entityToDto(this.postService.addCommentToPost(id, commentaryDto));
    }

    @GetMapping("/{id}/commentaries")
    public List<CommentaryDto> getComments(@PathVariable("id") String id) {
        return this.postService.getComments(id).stream().map(commentaryMapper::entityToDto).toList();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/commentaries/{comId}/up")
    public void upvoteComment(@PathVariable("id") String id, @PathVariable("comId") String commentId) {
        this.postService.upvote(commentId);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/commentaries/{comId}/down")
    public void downvoteComment(@PathVariable("id") String id, @PathVariable("comId") String commentId) {
        this.postService.downvote(commentId);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/commentaries/{comId}/un")
    public void unvoteComment(@PathVariable("id") String id, @PathVariable("comId") String commentId) {
        this.postService.unvote(commentId);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('JOURNALIST') and @postFilterBean.targetedCommentaryPublishedByIdEqualsLoggedInUserId())")
    @DeleteMapping("/{id}/commentaries/{comId}")
    public void deleteComment(@PathVariable("id") String id, @PathVariable("comId") String commentId) {
        this.postService.deleteComment(commentId);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('JOURNALIST') and @postFilterBean.targetedCommentaryPublishedByIdEqualsLoggedInUserId())")
    @PatchMapping("/{id}/commentaries/{comId}")
    public void editComment(@PathVariable("id") String id, @PathVariable("comId") String commentId, @RequestBody @Valid CommentaryDto commentaryDto) {
        this.postService.editCommentary(commentId, commentaryDto);
    }
}
