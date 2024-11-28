package at.ac.tuwien.verifeed.core.dto.recognition;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostDto {
    private String postId;
    private String url;
    private Date createdAt;
    private AccountDto account;
    private String content;
    private PostMetricsDto metrics;
    private ParentPostDto parentPost;
    private List<HashtagDto> hashtags;
    private List<MediaAttachmentDto> mediaAttachments;
    private boolean edited;
    private Long topic;

    // This ID is assigned by core when it sends post from the past x days to
    // the recognition service to check for duplicates.
    // When the recognition service sends back the grouped posts, we can
    // identify the posts we already persisted.
    private String internalPostId;
}
