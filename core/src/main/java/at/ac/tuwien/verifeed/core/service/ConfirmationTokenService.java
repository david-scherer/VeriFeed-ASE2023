package at.ac.tuwien.verifeed.core.service;

import at.ac.tuwien.verifeed.core.entities.ConfirmationToken;
import at.ac.tuwien.verifeed.core.entities.VerifeedUser;

public interface ConfirmationTokenService {
    void saveConfirmationToken(ConfirmationToken confirmationToken);

    ConfirmationToken getToken(String token);

    int setConfirmationDate(String token);

    void deleteTokenFor(VerifeedUser user);
}
