package at.ac.tuwien.verifeed.core.mapper;

import at.ac.tuwien.verifeed.core.dto.VerifeedUserDto;
import at.ac.tuwien.verifeed.core.entities.VerifeedUser;

public interface VerifeedUserMapper {
    VerifeedUserDto entityToDto(VerifeedUser verifeedUser);
}
