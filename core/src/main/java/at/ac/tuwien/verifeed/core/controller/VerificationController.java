package at.ac.tuwien.verifeed.core.controller;

import at.ac.tuwien.verifeed.core.dto.VerificationRequestAnswerDto;
import at.ac.tuwien.verifeed.core.dto.VerificationRequestDto;
import at.ac.tuwien.verifeed.core.mapper.impl.VerificationRequestMapperImpl;
import at.ac.tuwien.verifeed.core.service.VerificationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("${web.api.base}/verification")
public class VerificationController {

    private final Logger logger = LoggerFactory.getLogger(VerificationController.class);
    private final VerificationService verificationService;

    private final VerificationRequestMapperImpl verificationRequestMapper;

    @Autowired
    public VerificationController(VerificationService verificationService, VerificationRequestMapperImpl verificationRequestMapper) {
        this.verificationService = verificationService;
        this.verificationRequestMapper = verificationRequestMapper;
    }

    @PreAuthorize("hasRole('ROLE_JOURNALIST')")
    @GetMapping
    public List<VerificationRequestAnswerDto> getVerificationRequest() {
        logger.info("Get Verification Request");
        return verificationService.getVerificationRequest().stream().map(verificationRequestMapper::entityToDto).toList();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public void createVerificationRequest(@RequestBody @Valid VerificationRequestDto verificationRequestDto) {
        logger.info("Create Verification Request");
        verificationService.createVerificationRequest(verificationRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_JOURNALIST')")
    @PutMapping("/grant/{id}")
    public void grantVerificationRequest(@PathVariable("id") UUID uuid) {
        logger.info("Grant Verification Request {}", uuid);
        verificationService.grantVerificationRequest(uuid);
    }

    @PreAuthorize("hasRole('ROLE_JOURNALIST')")
    @PutMapping("/decline/{id}")
    public void declineVerificationRequest(@PathVariable("id") UUID uuid) {
        logger.info("Decline Verification Request {}", uuid);
        verificationService.declineVerificationRequest(uuid);
    }
}
