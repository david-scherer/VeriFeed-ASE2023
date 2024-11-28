package at.ac.tuwien.verifeed.core.controller;

import at.ac.tuwien.verifeed.core.dto.CertificateDto;
import at.ac.tuwien.verifeed.core.dto.CertificateRequest;
import at.ac.tuwien.verifeed.core.mapper.impl.CertificateMapperImpl;
import at.ac.tuwien.verifeed.core.service.CertificateService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${web.api.base}/certificate")
public class CertificateController {

    private final Logger logger = LoggerFactory.getLogger(CertificateController.class);
    private final CertificateService certificateService;

    private final CertificateMapperImpl certificateMapper;

    @Autowired
    public CertificateController(CertificateService certificateService, CertificateMapperImpl certificateMapper) {
        this.certificateService = certificateService;
        this.certificateMapper = certificateMapper;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void revokeCertificate(@PathVariable("id") String id) {
        logger.info("Revoke Certificate´{} Request", id);
        certificateService.revokeCertificate(UUID.fromString(id));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public List<CertificateDto> getCertificates() {
        logger.info("Get Certificates Request");
        return certificateService.getCertificates().stream().map(certificateMapper::entityToDto).toList();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public void createCertificate(@RequestBody @Valid CertificateRequest request) {
        logger.info("Create Certificate Request for journalist {}", request.getId());
        certificateService.createCertificate(request);
    }
}
