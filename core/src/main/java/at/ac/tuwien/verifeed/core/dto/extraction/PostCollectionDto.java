package at.ac.tuwien.verifeed.core.dto.extraction;

import at.ac.tuwien.verifeed.core.dto.recognition.SourceDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostCollectionDto {
    private String triggerTimestamp;
    private String postCollectionId;
    private SourceDto source;
    private Date createdAt;
}
