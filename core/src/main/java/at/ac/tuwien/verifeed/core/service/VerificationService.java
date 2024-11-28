package at.ac.tuwien.verifeed.core.service;

import at.ac.tuwien.verifeed.core.dto.VerificationRequestAnswerDto;
import at.ac.tuwien.verifeed.core.dto.VerificationRequestDto;
import at.ac.tuwien.verifeed.core.entities.VerificationRequest;

import java.util.List;
import java.util.UUID;

public interface VerificationService {
    List<VerificationRequest> getVerificationRequest();

    void createVerificationRequest(VerificationRequestDto verificationRequestDto);

    void grantVerificationRequest(UUID uuid);

    void declineVerificationRequest(UUID uuid);
}
