package at.ac.tuwien.verifeed.core.controller;

import at.ac.tuwien.verifeed.core.dto.AffiliationDto;
import at.ac.tuwien.verifeed.core.dto.AffiliationRequest;
import at.ac.tuwien.verifeed.core.exception.EntityNotFoundException;
import at.ac.tuwien.verifeed.core.exception.MissingPermissionException;
import at.ac.tuwien.verifeed.core.mapper.impl.AffiliationMapperImpl;
import at.ac.tuwien.verifeed.core.service.AffiliationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${web.api.base}/affiliation")
public class AffiliationController {

    private final Logger logger = LoggerFactory.getLogger(AffiliationController.class);

    private final AffiliationMapperImpl affiliationMapper;

    private final AffiliationService affiliationService;

    @Autowired
    public AffiliationController(AffiliationMapperImpl affiliationMapper, AffiliationService affiliationService) {
        this.affiliationMapper = affiliationMapper;
        this.affiliationService = affiliationService;
    }

    @PreAuthorize("hasRole('ROLE_JOURNALIST')")
    @PostMapping
    public void createAffiliation(@RequestBody @Valid AffiliationRequest request) throws EntityNotFoundException {
        logger.info("Create Affiliation Request for {}", request.getName());
        affiliationService.createAffiliation(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/verify")
    public void verifyAffiliation(@PathVariable("id") String id) throws EntityNotFoundException {
        logger.info("Verification Affiliation Request for {}", id);
        affiliationService.verifyAffiliation(UUID.fromString(id));
    }

    @PreAuthorize("hasRole('ROLE_JOURNALIST')")
    @PutMapping("/add/{journalistId}")
    public void addJournalistToAffiliation(@PathVariable("journalistId") String journalistId) throws EntityNotFoundException, MissingPermissionException {
        logger.info("Add Journalist {} to affiliation", journalistId);
        affiliationService.addJournalistToAffiliation(UUID.fromString(journalistId));
    }

    @PreAuthorize("hasRole('ROLE_JOURNALIST')")
    @DeleteMapping("/remove/{journalistId}")
    public void removeJournalistFromAffiliation(@PathVariable("journalistId") String journalistId) throws EntityNotFoundException {
        logger.info("Remove Journalist {} from affiliation", journalistId);
        affiliationService.removeJournalistFromAffiliation(UUID.fromString(journalistId));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public AffiliationDto getAffiliationById(@PathVariable("id") String id) throws EntityNotFoundException {
        logger.info("Get Affiliation Request for {}", id);
        return affiliationMapper.entityToDto(affiliationService.getAffiliationById(UUID.fromString(id)));
    }

    @PreAuthorize("hasRole('ROLE_JOURNALIST')")
    @GetMapping("/user/{id}")
    public boolean belongsJournalistToAffiliation(@PathVariable("id") String id) throws EntityNotFoundException {
        logger.info("Is Affiliation Request for {}", id);
        return affiliationService.belongsJournalistToAffiliation(UUID.fromString(id));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/verified")
    public List<AffiliationDto> getVerifiedAffiliations() {
        logger.info("Get Verified Affiliations Request");
        return affiliationService.getVerifiedAffiliations().stream().map(affiliationMapper :: entityToDto).toList();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/unverified")
    public List<AffiliationDto> getUnverifiedAffiliations() throws MissingPermissionException {
        logger.info("Get Unverified Affiliations Request");
        return affiliationService.getUnverifiedAffiliations().stream().map(affiliationMapper :: entityToDto).toList();
    }
}
