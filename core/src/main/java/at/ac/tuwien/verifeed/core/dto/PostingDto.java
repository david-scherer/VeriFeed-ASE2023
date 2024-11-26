package at.ac.tuwien.verifeed.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PostingDto {

    private UUID id;

    private String summary;

    private LocalDateTime published;

    private TopicDto topic;

    private PlatformDto originatesFrom;

    private List<SourceDto> sources;
}
