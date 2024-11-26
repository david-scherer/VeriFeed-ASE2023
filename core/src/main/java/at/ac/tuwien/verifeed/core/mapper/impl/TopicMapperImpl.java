package at.ac.tuwien.verifeed.core.mapper.impl;

import at.ac.tuwien.verifeed.core.dto.TopicDto;
import at.ac.tuwien.verifeed.core.entities.Topic;
import at.ac.tuwien.verifeed.core.mapper.TopicMapper;
import org.springframework.stereotype.Component;

@Component
public class TopicMapperImpl implements TopicMapper {
    @Override
    public TopicDto entityToDto(Topic topic) {
        return TopicDto.builder().id(topic.getId()).name(topic.getName()).build();
    }
}
