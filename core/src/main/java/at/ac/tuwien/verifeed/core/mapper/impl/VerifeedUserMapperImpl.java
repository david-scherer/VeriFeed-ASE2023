package at.ac.tuwien.verifeed.core.mapper.impl;

import at.ac.tuwien.verifeed.core.dto.VerifeedUserDto;
import at.ac.tuwien.verifeed.core.entities.Journalist;
import at.ac.tuwien.verifeed.core.entities.VerifeedUser;
import at.ac.tuwien.verifeed.core.mapper.VerifeedUserMapper;
import at.ac.tuwien.verifeed.core.repositories.JournalistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VerifeedUserMapperImpl implements VerifeedUserMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifeedUserMapperImpl.class);
    private static final String LOGGER_INFO = "(Mapper) " + VerifeedUserMapperImpl.class.getName() + " ";
    private final JournalistRepository journalistRepository;

    VerifeedUserMapperImpl(JournalistRepository journalistRepository) {
        this.journalistRepository = journalistRepository;
    }

    @Override
    public VerifeedUserDto entityToDto(VerifeedUser verifeedUser) {
        LOGGER.trace(LOGGER_INFO + "entityToDto");
        Optional<Journalist> journalist = journalistRepository.findById(verifeedUser.getId());
        boolean adminOf = false;
        if (journalist.isPresent()) {
            adminOf = journalist.get().getAdminOf() != null;
        }
        return new VerifeedUserDto(verifeedUser.getId(), verifeedUser.getUsername(), verifeedUser.getEmail(), verifeedUser.isLocked(), verifeedUser.isEnabled(), adminOf, verifeedUser.getImg());
    }
}