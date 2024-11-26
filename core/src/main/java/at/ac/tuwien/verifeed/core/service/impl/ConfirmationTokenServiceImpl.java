package at.ac.tuwien.verifeed.core.service.impl;

import at.ac.tuwien.verifeed.core.entities.ConfirmationToken;
import at.ac.tuwien.verifeed.core.entities.VerifeedUser;
import at.ac.tuwien.verifeed.core.repositories.ConfirmationTokenRepository;
import at.ac.tuwien.verifeed.core.service.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public ConfirmationToken getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    @Override
    public int setConfirmationDate(String token) {
        return confirmationTokenRepository.updateConfirmationDate(token, LocalDateTime.now());
    }

    @Override
    public void deleteTokenFor(VerifeedUser user) {
        confirmationTokenRepository.deleteByUser(user);
    }
}
