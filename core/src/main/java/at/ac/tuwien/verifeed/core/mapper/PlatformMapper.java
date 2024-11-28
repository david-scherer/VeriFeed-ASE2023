package at.ac.tuwien.verifeed.core.mapper;

import at.ac.tuwien.verifeed.core.dto.PlatformDto;
import at.ac.tuwien.verifeed.core.entities.Platform;

public interface PlatformMapper {

    PlatformDto entityToDto(Platform platform);

}
