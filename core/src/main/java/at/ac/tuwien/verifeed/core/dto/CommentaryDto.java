package at.ac.tuwien.verifeed.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentaryDto {

    private UUID id;

    private Integer version;

    private Integer score;

    private Integer vote;

    private String text;

    private LocalDateTime published;

    private JournalistDto publishedBy;

    private PostingDto relatedPost;

    private List<SourceDto> sources;
}
