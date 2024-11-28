package at.ac.tuwien.verifeed.core.mapper.impl;

import at.ac.tuwien.verifeed.core.dto.CertificateDto;
import at.ac.tuwien.verifeed.core.dto.CertificateJournalistDto;
import at.ac.tuwien.verifeed.core.entities.Certificate;
import at.ac.tuwien.verifeed.core.entities.Journalist;
import at.ac.tuwien.verifeed.core.mapper.CertificateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CertificateMapperImpl implements CertificateMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(CertificateMapperImpl.class);
    private static final String LOGGER_INFO = "(Mapper) " + CertificateMapperImpl.class.getName() + " ";

    @Override
    public CertificateDto entityToDto(Certificate certificate) {
        LOGGER.trace(LOGGER_INFO + "entityToDto");
        List<CertificateJournalistDto> certificateJournalistDtos = new ArrayList<>();
        for (Journalist journalist : certificate.getVerifiedJournalists()) {
            certificateJournalistDtos.add(new CertificateJournalistDto(journalist.getId(), journalist.getFirstName(), journalist.getLastName(), journalist.getDateOfBirth(), journalist.getScore(), journalist.isVerified(), journalist.getJournalistVerificationDetails()));
        }
        CertificateJournalistDto certificateHolder = null;
        if (certificate.getCertificateHolder() != null) {
            certificateHolder = new CertificateJournalistDto(certificate.getCertificateHolder().getId(), certificate.getCertificateHolder().getFirstName(), certificate.getCertificateHolder().getLastName(), certificate.getCertificateHolder().getDateOfBirth(), certificate.getCertificateHolder().getScore(), certificate.getCertificateHolder().isVerified(), certificate.getCertificateHolder().getJournalistVerificationDetails());
        }
        return new CertificateDto(certificate.getId(), certificate.getReference(), certificate.getExplanation(), certificateHolder, certificateJournalistDtos, certificate.getVerificationRequests());
    }
}