package at.ac.tuwien.verifeed.core.mapper.impl;

import at.ac.tuwien.verifeed.core.dto.PostingDto;
import at.ac.tuwien.verifeed.core.entities.Posting;
import at.ac.tuwien.verifeed.core.entities.Source;
import at.ac.tuwien.verifeed.core.mapper.PlatformMapper;
import at.ac.tuwien.verifeed.core.mapper.PostingMapper;
import at.ac.tuwien.verifeed.core.mapper.SourceMapper;
import at.ac.tuwien.verifeed.core.mapper.TopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostingMapperImpl implements PostingMapper {

    private final PlatformMapper platformMapper;

    private final SourceMapper sourceMapper;

    private final TopicMapper topicMapper;

    @Autowired
    public PostingMapperImpl(PlatformMapper platformMapper, SourceMapper sourceMapper, TopicMapper topicMapper) {
        this.platformMapper = platformMapper;
        this.sourceMapper = sourceMapper;
        this.topicMapper = topicMapper;
    }

    @Override
    public PostingDto entityToDto(Posting posting) {

        return PostingDto.builder()
                .topic(topicMapper.entityToDto(posting.getTopic()))
                .sources(posting.getSources().stream().map(sourceMapper::entityToDto).toList())
                .summary(posting.getSummary())
                .id(posting.getId())
                .originatesFrom(platformMapper.entityToDto(posting.getOriginatesFrom()))
                .published(posting.getPublished())
                .build();
    }
}
