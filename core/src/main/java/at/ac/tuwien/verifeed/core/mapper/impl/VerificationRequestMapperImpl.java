package at.ac.tuwien.verifeed.core.mapper.impl;

import at.ac.tuwien.verifeed.core.dto.VerificationRequestAnswerDto;
import at.ac.tuwien.verifeed.core.entities.VerificationRequest;
import at.ac.tuwien.verifeed.core.mapper.VerificationRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VerificationRequestMapperImpl implements VerificationRequestMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerificationRequestMapperImpl.class);
    private static final String LOGGER_INFO = "(Mapper) " + VerificationRequestMapperImpl.class.getName() + " ";
    private final CertificateMapperImpl certificateMapper;
    private final VerifeedUserMapperImpl verifeedUserMapper;

    public VerificationRequestMapperImpl(CertificateMapperImpl certificateMapper, VerifeedUserMapperImpl verifeedUserMapper) {
        this.certificateMapper = certificateMapper;
        this.verifeedUserMapper = verifeedUserMapper;
    }

    @Override
    public VerificationRequestAnswerDto entityToDto(VerificationRequest verificationRequest) {
        LOGGER.trace(LOGGER_INFO + "entityToDto");
        return new VerificationRequestAnswerDto(verificationRequest.getId(), verifeedUserMapper.entityToDto(verificationRequest.getRequester()), certificateMapper.entityToDto(verificationRequest.getUsedCertificate()), verificationRequest.getStatus(), verificationRequest.getFirstName(), verificationRequest.getLastName(), verificationRequest.getDateOfBirth(), verificationRequest.getAddress(), verificationRequest.getEmployer(), verificationRequest.getDistributionReach(), verificationRequest.getMainMedium(), verificationRequest.getReference(), verificationRequest.getRequestMessage());
    }
}