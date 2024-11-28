package at.ac.tuwien.verifeed.core.mapper;

import at.ac.tuwien.verifeed.core.dto.VerificationRequestAnswerDto;
import at.ac.tuwien.verifeed.core.entities.VerificationRequest;

public interface VerificationRequestMapper {
    VerificationRequestAnswerDto entityToDto(VerificationRequest verificationRequest);
}
