package at.ac.tuwien.verifeed.core.dto.recognition;

import at.ac.tuwien.verifeed.core.dto.extraction.PostCollectionDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GroupedPostCollectionDto extends PostCollectionDto {
    private List<List<PostDto>> posts;
}
