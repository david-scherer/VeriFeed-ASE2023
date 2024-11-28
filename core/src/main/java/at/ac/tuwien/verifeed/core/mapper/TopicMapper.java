package at.ac.tuwien.verifeed.core.mapper;

import at.ac.tuwien.verifeed.core.dto.TopicDto;
import at.ac.tuwien.verifeed.core.entities.Topic;

public interface TopicMapper {

    TopicDto entityToDto(Topic topic);

}
