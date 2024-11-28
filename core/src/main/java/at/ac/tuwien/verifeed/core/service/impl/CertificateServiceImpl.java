package at.ac.tuwien.verifeed.core.service.impl;

import at.ac.tuwien.verifeed.core.dto.CertificateRequest;
import at.ac.tuwien.verifeed.core.entities.Certificate;
import at.ac.tuwien.verifeed.core.entities.Journalist;
import at.ac.tuwien.verifeed.core.exception.DuplicateEntityFoundException;
import at.ac.tuwien.verifeed.core.exception.EntityNotFoundException;
import at.ac.tuwien.verifeed.core.mapper.impl.CertificateMapperImpl;
import at.ac.tuwien.verifeed.core.repositories.CertificateRepository;
import at.ac.tuwien.verifeed.core.repositories.JournalistRepository;
import at.ac.tuwien.verifeed.core.service.CertificateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CertificateServiceImpl implements CertificateService {
    Logger logger = LoggerFactory.getLogger(CertificateServiceImpl.class);
    private final CertificateRepository certificateRepository;
    private final JournalistRepository journalistRepository;
    private final CertificateMapperImpl certificateMapper;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository, JournalistRepository journalistRepository, CertificateMapperImpl certificateMapper) {
        this.certificateRepository = certificateRepository;
        this.journalistRepository = journalistRepository;
        this.certificateMapper = certificateMapper;
    }

    @Override
    public List<Certificate> getCertificates() {
        return certificateRepository.findAll();
    }

    @Override
    public void createCertificate(Journalist journalist, String explanation, URL reference) {
        Certificate certificate = new Certificate();
        certificate.setCertificateHolder(journalist);
        certificate.setExplanation(explanation);
        certificate.setReference(reference);
        certificate = certificateRepository.save(certificate);
        logger.info("Certificate " + certificate.getId() + " created.");
    }

    @Override
    public void createCertificate(CertificateRequest certificateRequest) {
        Certificate certificate = new Certificate();
        Optional<Journalist> journalist = journalistRepository.findById(UUID.fromString(certificateRequest.getId()));
        if (journalist.isEmpty()) {
            throw new EntityNotFoundException("Journalist not found.");
        }
        if (journalist.get().getHolderOfCertificate() != null) {
            throw new DuplicateEntityFoundException("Certificate for this journalist already exists");
        }
        certificate.setCertificateHolder(journalist.get());
        certificate.setReference(certificateRequest.getReference());
        certificate.setExplanation(certificateRequest.getExplanation());
        certificate = certificateRepository.save(certificate);
        logger.info("Certificate " + certificate.getId() + " created.");
    }

    @Override
    public void revokeCertificate(UUID uuid) {
        Optional<Certificate> certificate = certificateRepository.findById(uuid);
        if (certificate.isEmpty()) {
            throw new EntityNotFoundException("Certificate not found.");
        }
        List<Journalist> journalists = certificate.get().getVerifiedJournalists();
        journalists.add(certificate.get().getCertificateHolder());
        for (Journalist journalist : journalists) {
            journalist.setVerified(false);
            journalist.setVerifiedBy(null);
            journalistRepository.save(journalist);
        }
        certificateRepository.deleteById(uuid);
        logger.info("Certificate " + uuid + " deleted.");
    }

}
