package at.ac.tuwien.verifeed.core.datagen;

import at.ac.tuwien.verifeed.core.entities.Affiliation;
import at.ac.tuwien.verifeed.core.entities.Journalist;
import at.ac.tuwien.verifeed.core.entities.JournalistVerificationDetails;
import at.ac.tuwien.verifeed.core.entities.VerifeedUser;
import at.ac.tuwien.verifeed.core.enums.UserRole;
import at.ac.tuwien.verifeed.core.repositories.AffiliationRepository;
import at.ac.tuwien.verifeed.core.repositories.JournalistRepository;
import at.ac.tuwien.verifeed.core.repositories.JournalistVerificationDetailsRepository;
import at.ac.tuwien.verifeed.core.repositories.UserRepository;
import at.ac.tuwien.verifeed.core.service.UserService;
import at.ac.tuwien.verifeed.core.service.impl.CertificateServiceImpl;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestUserGenerator {

    private final Logger logger = LoggerFactory.getLogger(TestUserGenerator.class);

    private final UserRepository appUserRepository;

    private final CertificateServiceImpl certificateService;
    private final UserService userService;
    private final JournalistRepository journalistRepository;
    private final AffiliationRepository affiliationRepository;
    private final JournalistVerificationDetailsRepository journalistVerificationDetailsRepository;

    @Autowired
    public TestUserGenerator(UserRepository appUserRepository, CertificateServiceImpl certificateService, UserService userService, JournalistRepository journalistRepository, AffiliationRepository affiliationRepository, JournalistVerificationDetailsRepository journalistVerificationDetailsRepository) {
        this.appUserRepository = appUserRepository;
        this.certificateService = certificateService;
        this.userService = userService;
        this.journalistRepository = journalistRepository;
        this.affiliationRepository = affiliationRepository;
        this.journalistVerificationDetailsRepository = journalistVerificationDetailsRepository;
    }

    @PostConstruct
    private void initData() {
        initUserData();
    }

    private void initUserData() {
        String[] usernames = {"userverifeed", "truthseeker", "franzi96", "tom23"};
        for (String username : usernames) {
            registerTestUser(username);
        }
        String[] journalists = {"journalistverifeed", "journalist1", "journalist2", "journalist3"};
        registerTestJournalist(journalists[0], "Max", "Kaiser", "Kronen Zeitung", true);
        registerTestJournalist(journalists[1], "Sabine", "Huber", "DerStandard", false);
        registerTestJournalist(journalists[2], "Peter", "Baldi", "The Guardian", false);
        registerTestJournalist(journalists[3], "Franziska", "Glas", "BBC", true);
        List<Journalist> journalistList = new ArrayList<>();
        for (String journalist : journalists) {
            journalistList.add(journalistRepository.findByEmail(journalist + "@byom.de").get());
        }
        createTestAffiliation(journalistList, "Association of Journalists", journalistList.get(0), true);
        createTestAffiliation(null, "The Guardian", journalistList.get(2), false);
    }

    private void registerTestJournalist(String username, String firstName, String lastName, String mainMedium, boolean certified) {
        String mail = username + "@byom.de";
        if (appUserRepository.findByEmail(mail).isPresent()) return;
        Journalist journalist = new Journalist();
        journalist.setUserRole(UserRole.JOURNALIST);
        journalist.setUsername(username);
        journalist.setEmail(mail);
        journalist.setPassword("abc");
        journalist.setFirstName(firstName);
        journalist.setLastName(lastName);
        journalist.setEnabled(true);
        journalist.setLocked(false);
        journalist.setVerified(true);
        journalist.setDateOfBirth(LocalDate.parse("2000-01-01"));
        JournalistVerificationDetails journalistVerificationDetails = new JournalistVerificationDetails();
        journalistVerificationDetails.setEmployer(mainMedium);
        journalistVerificationDetails.setDistributionReach((int) (Math.random() * ((1000000 - 10000) + 1)) + 10000);
        journalistVerificationDetails.setMainMedium(mainMedium);
        try {
            journalistVerificationDetails.setReference(new URL("https://" + mainMedium.toLowerCase().replaceAll("\\s", "") + ".com"));
        } catch (MalformedURLException ignored) {
        }
        journalistVerificationDetails.setMessage("I hope this message finds you well. I am a journalist, currently working on a interesting topics trending on social media. I would like to request permission to publish these stories on your platform.\n" +
                "\n" +
                "The planned stories align with your platform readership and promise to captivate readers through uniqueness. I am available for further information and can adhere to your editorial guidelines.\n" +
                "\n" +
                "Thank you for your time and consideration.\n" +
                "\n" +
                "Best regards, " + firstName + " " + lastName);
        journalist.setJournalistVerificationDetails(journalistVerificationDetailsRepository.save(journalistVerificationDetails));
        userService.signUpUser(journalist);
        userService.enableAppUser(mail);
        journalist = journalistRepository.findByEmail(mail).get();
        if (certified) {
            certificateService.createCertificate(journalist, "Due to the reach and the outstanding critical articles, we grant this journalist a certification.", null);
        }
        logger.info("Test Journalist " + firstName + " " + lastName + " registered (" + journalist.getId() + ")");
    }

    private void createTestAffiliation(List<Journalist> journalistList, String name, Journalist owner, boolean verified) {
        if (affiliationRepository.findByOwner(owner).isPresent()) return;
        Affiliation affiliation = new Affiliation();
        affiliation.setName(name);
        affiliation.setOwner(owner);
        affiliation.setVerified(verified);
        if (verified) {
            affiliation.setJournalists(journalistList);
        }
        try {
            affiliation.setAddress(new URL("https://" + name.toLowerCase().replaceAll("\\s", "") + ".com"));
        } catch (MalformedURLException ignored) {
        }
        affiliation = affiliationRepository.save(affiliation);
        logger.info("Test Affiliation " + name + " belonging to " +  owner.getFirstName() + " " + owner.getLastName() + " registered (" + affiliation.getId() + ")");
    }

    private void registerTestUser(String username) {
        String mail = username + "@byom.de";
        if (appUserRepository.findByEmail(mail).isPresent()) return;
        VerifeedUser user = new VerifeedUser();
        user.setUserRole(UserRole.USER);
        user.setUsername(username);
        user.setEmail(mail);
        user.setPassword("abc");
        user.setEnabled(true);
        user.setLocked(false);
        userService.signUpUser(user);
        userService.enableAppUser(mail);
        logger.info("Test User " + username + " registered");
    }
}
