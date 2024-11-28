package at.ac.tuwien.verifeed.core.mapper.impl;

import at.ac.tuwien.verifeed.core.dto.SourceDto;
import at.ac.tuwien.verifeed.core.entities.Source;
import at.ac.tuwien.verifeed.core.mapper.SourceMapper;
import org.springframework.stereotype.Component;

@Component
public class SourceMapperImpl implements SourceMapper {
    @Override
    public SourceDto entityToDto(Source source) {
        return new SourceDto(source.getId(), source.getLocation());
    }
}
