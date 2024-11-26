package at.ac.tuwien.verifeed.core.mapper.impl;

import at.ac.tuwien.verifeed.core.dto.JournalistAffiliationDto;
import at.ac.tuwien.verifeed.core.dto.JournalistDto;
import at.ac.tuwien.verifeed.core.entities.Affiliation;
import at.ac.tuwien.verifeed.core.entities.Journalist;
import at.ac.tuwien.verifeed.core.mapper.JournalistMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JournalistMapperImpl implements JournalistMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(JournalistMapperImpl.class);
    private static final String LOGGER_INFO = "(Mapper) " + JournalistMapperImpl.class.getName() + " ";

    @Override
    public JournalistDto entityToDto(Journalist journalist) {
        LOGGER.trace(LOGGER_INFO + "entityToDto");
        List<JournalistAffiliationDto> journalistAffiliationDtoList = new ArrayList<>();
        for (Affiliation affiliation : journalist.getPublishesFor()) {
            journalistAffiliationDtoList.add(new JournalistAffiliationDto(affiliation.getId(),affiliation.getName(), affiliation.getAddress(), affiliation.isVerified()));
        }
        JournalistAffiliationDto adminOf = null;
        if (journalist.getAdminOf() != null) {
            adminOf = new JournalistAffiliationDto(journalist.getAdminOf().getId(),journalist.getAdminOf().getName(), journalist.getAdminOf().getAddress(), journalist.getAdminOf().isVerified());
        }
        return new JournalistDto(journalist.getId(), journalist.getFirstName(), journalist.getLastName(), journalist.getDateOfBirth(), journalist.getScore(), journalist.isVerified(), adminOf, journalistAffiliationDtoList, journalist.getAddress(), journalist.getJournalistVerificationDetails(), journalist.getImg());
    }
}