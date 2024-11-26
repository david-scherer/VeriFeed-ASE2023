package at.ac.tuwien.verifeed.core.mapper.impl;

import at.ac.tuwien.verifeed.core.dto.PlatformDto;
import at.ac.tuwien.verifeed.core.entities.Platform;
import at.ac.tuwien.verifeed.core.mapper.PlatformMapper;
import org.springframework.stereotype.Component;

@Component
public class PlatformMapperImpl implements PlatformMapper {
    @Override
    public PlatformDto entityToDto(Platform platform) {
        return new PlatformDto(platform.getId(), platform.getName(), platform.getServiceId());
    }
}
