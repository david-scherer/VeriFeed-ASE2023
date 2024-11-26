package at.ac.tuwien.verifeed.core.mapper;

import at.ac.tuwien.verifeed.core.dto.SourceDto;
import at.ac.tuwien.verifeed.core.entities.Source;

public interface SourceMapper {

    SourceDto entityToDto(Source source);

}
