package at.ac.tuwien.verifeed.core.controller;

import at.ac.tuwien.verifeed.core.dto.JournalistDto;
import at.ac.tuwien.verifeed.core.exception.EntityNotFoundException;
import at.ac.tuwien.verifeed.core.exception.MissingPermissionException;
import at.ac.tuwien.verifeed.core.mapper.impl.JournalistMapperImpl;
import at.ac.tuwien.verifeed.core.service.JournalistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${web.api.base}/journalist")
public class JournalistController {

    private final Logger logger = LoggerFactory.getLogger(JournalistController.class);
    private final JournalistService journalistService;

    private final JournalistMapperImpl journalistMapper;

    @Autowired
    public JournalistController(JournalistService journalistService, JournalistMapperImpl journalistMapper) {
        this.journalistService = journalistService;
        this.journalistMapper = journalistMapper;
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public JournalistDto getJournalistById(@PathVariable("id") String id) throws EntityNotFoundException {
        logger.info("Get Journalist Request for {}", id);
        return journalistMapper.entityToDto(journalistService.getJournalistById(UUID.fromString(id)));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/verified")
    public List<JournalistDto> getVerifiedJournalists() {
        logger.info("Get Verified Journalists Request");
        return journalistService.getVerifiedJournalists().stream().map(journalistMapper::entityToDto).toList();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/verified&certified")
    public List<JournalistDto> getVerifiedCertifiedJournalists() {
        logger.info("Get Verified and Certified Journalists Request");
        return journalistService.getVerifiedCertifiedJournalists().stream().map(journalistMapper::entityToDto).toList();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/verified&uncertified")
    public List<JournalistDto> getVerifiedUncertifiedJournalists() {
        logger.info("Get Verified and Uncertified Journalists Request");
        return journalistService.getVerifiedUncertifiedJournalists().stream().map(journalistMapper::entityToDto).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unverified")
    public List<JournalistDto> getUnverifiedJournalists() throws MissingPermissionException{
        logger.info("Get Unverified Journalists Request");
        return journalistService.getUnverifiedJournalists().stream().map(journalistMapper::entityToDto).toList();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/verify/{id}")
    public void verifyJournalist(@PathVariable("id") UUID uuid) {
        logger.info("Verify Journalist with id {}", uuid);
        journalistService.verifyJournalist(uuid);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/unverify/{id}")
    public void unverifyJournalist(@PathVariable("id") UUID uuid) {
        logger.info("Unverify Journalist with id {}", uuid);
        journalistService.unverifyJournalist(uuid);
    }
}
