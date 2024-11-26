package at.ac.tuwien.verifeed.core.dto.recognition;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MediaAttachmentDto {
    private String mediaAttachmentId;
    private String type;
    private String url;
}
