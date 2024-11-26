package at.ac.tuwien.verifeed.core.service;

import at.ac.tuwien.verifeed.core.dto.RegistrationRequest;
import jakarta.transaction.Transactional;

public interface RegistrationService {
    String register(RegistrationRequest request);

    @Transactional
    void confirmToken(String token);
}
