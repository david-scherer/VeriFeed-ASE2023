package at.ac.tuwien.verifeed.core.mapper;

import at.ac.tuwien.verifeed.core.dto.JournalistDto;
import at.ac.tuwien.verifeed.core.entities.Journalist;

public interface JournalistMapper {
    JournalistDto entityToDto(Journalist journalist);
}
