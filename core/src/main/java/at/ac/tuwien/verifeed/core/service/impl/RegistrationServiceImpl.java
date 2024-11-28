package at.ac.tuwien.verifeed.core.service.impl;

import at.ac.tuwien.verifeed.core.dto.RegistrationRequest;
import at.ac.tuwien.verifeed.core.email.EMailSender;
import at.ac.tuwien.verifeed.core.email.MailConfig;
import at.ac.tuwien.verifeed.core.email.StaticHTMLProvider;
import at.ac.tuwien.verifeed.core.entities.ConfirmationToken;
import at.ac.tuwien.verifeed.core.entities.VerifeedUser;
import at.ac.tuwien.verifeed.core.enums.UserRole;
import at.ac.tuwien.verifeed.core.exception.MailConfirmationException;
import at.ac.tuwien.verifeed.core.service.ConfirmationTokenService;
import at.ac.tuwien.verifeed.core.service.RegistrationService;
import at.ac.tuwien.verifeed.core.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final MailConfig mailConfig;
    private final EMailSender sender;

    @Autowired
    public RegistrationServiceImpl(UserService userService, ConfirmationTokenService confirmationTokenService, MailConfig mailConfig, EMailSender sender) {
        this.userService = userService;
        this.confirmationTokenService = confirmationTokenService;
        this.mailConfig = mailConfig;
        this.sender = sender;
    }

    @Override
    public String register(RegistrationRequest request) {

        // receive token and send it via mail to new user
        String token = userService.signUpUser(
                new VerifeedUser(request.getUsername(), request.getEmail(), request.getPassword(), UserRole.USER));
        String confirmationLink = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        sender.send(request.getEmail(), StaticHTMLProvider.confirmationMail(confirmationLink));
        return token;
    }

    @Override
    @Transactional
    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token);
        if (confirmationToken == null) {
            throw new IllegalStateException("Token not found");
        }

        if (confirmationToken.getConfirmationDate() != null) {
            throw new MailConfirmationException("Mail is already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpirationDate();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmationDate(token);
        userService.enableAppUser(
                confirmationToken.getUser().getEmail());
    }

}
