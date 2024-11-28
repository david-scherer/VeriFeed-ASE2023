package at.ac.tuwien.verifeed.core.mapper;

import at.ac.tuwien.verifeed.core.dto.AffiliationDto;
import at.ac.tuwien.verifeed.core.entities.Affiliation;

public interface AffiliationMapper {
    AffiliationDto entityToDto(Affiliation affiliation);
}
