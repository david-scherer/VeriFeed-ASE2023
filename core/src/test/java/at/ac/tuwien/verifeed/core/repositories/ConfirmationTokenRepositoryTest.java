package at.ac.tuwien.verifeed.core.repositories;

import at.ac.tuwien.verifeed.core.entities.ConfirmationToken;
import at.ac.tuwien.verifeed.core.util.DatabaseInitializer;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(initializers = {DatabaseInitializer.class})
public class ConfirmationTokenRepositoryTest {

    @Autowired
    private ConfirmationTokenRepository tokenRepository;

    @Test
    @Transactional
    public void testStoreToken() {
        // given
        ConfirmationToken token = createRandomToken();

        // when
        ConfirmationToken storedToken = tokenRepository.save(token);

        // then
        assertConfirmation(storedToken, token);
    }

    @Test
    @Transactional
    public void testFindByToken() {
        // given
        ConfirmationToken token = createRandomToken();
        tokenRepository.save(token);

        // when
        ConfirmationToken storedToken = tokenRepository.findByToken(token.getToken());

        // then
        assertNotNull(storedToken);
        assertConfirmation(storedToken, token);
    }

    @Test
    @Transactional
    public void testConfirmToken() {
        // given
        ConfirmationToken token = createRandomToken();
        //token.setConfirmationDate(null);
        ConfirmationToken storedToken = tokenRepository.save(token);
        LocalDateTime confirmationDate = LocalDateTime.now();

        // when
        tokenRepository.updateConfirmationDate(storedToken.getToken(), confirmationDate);

        // then
        ConfirmationToken confirmedToken = tokenRepository.findByToken(storedToken.getToken());
        assertNotNull(confirmedToken.getConfirmationDate());
    }

    @Test
    @Transactional
    public void testDeleteToken() {
        // given
        ConfirmationToken token = createRandomToken();
        tokenRepository.save(token);
        ConfirmationToken storedToken = tokenRepository.findByToken(token.getToken());

        // when
        tokenRepository.deleteById(storedToken.getId());

        // then
        ConfirmationToken deletedToken = tokenRepository.findByToken(token.getToken());
        assertNull(deletedToken);
    }

    private ConfirmationToken createRandomToken() {
        ConfirmationToken token = new ConfirmationToken();
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(3);
        LocalDateTime creationDate = LocalDateTime.now();
        LocalDateTime confirmationDate = LocalDateTime.now().plusDays(1);
        String tokenString = UUID.randomUUID().toString();

        token.setToken(tokenString);
        token.setCreationDate(creationDate);
        token.setConfirmationDate(confirmationDate);
        token.setExpirationDate(expirationDate);
        return token;
    }

    private void assertConfirmation(ConfirmationToken token1, ConfirmationToken token2) {
        assertNotNull(token1);
        assertNotNull(token1.getId());
        assertEquals(token1.getConfirmationDate(), token2.getConfirmationDate());
        assertEquals(token1.getExpirationDate(), token2.getExpirationDate());
        assertEquals(token1.getCreationDate(), token2.getCreationDate());
    }
}
