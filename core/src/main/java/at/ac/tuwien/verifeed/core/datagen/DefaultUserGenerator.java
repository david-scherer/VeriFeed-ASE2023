package at.ac.tuwien.verifeed.core.datagen;

import at.ac.tuwien.verifeed.core.entities.VerifeedUser;
import at.ac.tuwien.verifeed.core.enums.UserRole;
import at.ac.tuwien.verifeed.core.repositories.UserRepository;
import at.ac.tuwien.verifeed.core.service.UserService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserGenerator {

    private final UserRepository appUserRepository;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(at.ac.tuwien.verifeed.core.datagen.DefaultUserGenerator.class);

    @Autowired
    public DefaultUserGenerator(UserRepository appUserRepository, UserService userService) {
        this.appUserRepository = appUserRepository;
        this.userService = userService;
    }

    @PostConstruct
    public void initData() {
        registerDefaultAdmin();
    }

    private void registerDefaultAdmin() {
        String mail = "adminverifeed@byom.de";
        if (appUserRepository.findByEmail(mail).isPresent()) return;
        VerifeedUser admin = new VerifeedUser();
        admin.setUserRole(UserRole.ADMIN);
        admin.setUsername("admin");
        admin.setEmail(mail);
        admin.setPassword("abc");
        admin.setEnabled(true);
        admin.setLocked(false);
        userService.signUpUser(admin);
        userService.enableAppUser(mail);
        logger.info("Default Admin registered");
    }
}
