package at.ac.tuwien.verifeed.core.mapper.impl;

import at.ac.tuwien.verifeed.core.dto.AffiliationDto;
import at.ac.tuwien.verifeed.core.dto.AffiliationJournalistDto;
import at.ac.tuwien.verifeed.core.entities.Affiliation;
import at.ac.tuwien.verifeed.core.entities.Journalist;
import at.ac.tuwien.verifeed.core.mapper.AffiliationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AffiliationMapperImpl implements AffiliationMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(AffiliationMapperImpl.class);
    private static final String LOGGER_INFO = "(Mapper) " + AffiliationMapperImpl.class.getName() + " ";

    @Override
    public AffiliationDto entityToDto(Affiliation affiliation) {
        LOGGER.trace(LOGGER_INFO + "entityToDto");
        List<AffiliationJournalistDto> affiliationJournalistDtoList = new ArrayList<>();
        for (Journalist journalist : affiliation.getJournalists()) {
            affiliationJournalistDtoList.add(new AffiliationJournalistDto(journalist.getId(), journalist.getFirstName(), journalist.getLastName(), journalist.getDateOfBirth(), journalist.getScore(), journalist.isVerified(), journalist.getJournalistVerificationDetails()));
        }
        AffiliationJournalistDto owner = null;
        if (affiliation.getOwner() != null) {
            owner = new AffiliationJournalistDto(affiliation.getOwner().getId(), affiliation.getOwner().getFirstName(), affiliation.getOwner().getLastName(), affiliation.getOwner().getDateOfBirth(), affiliation.getOwner().getScore(), affiliation.getOwner().isVerified(), affiliation.getOwner().getJournalistVerificationDetails());
        }
        return new AffiliationDto(affiliation.getId(), affiliation.getName(), affiliation.getAddress(), affiliation.isVerified(), owner, affiliationJournalistDtoList);
    }
}