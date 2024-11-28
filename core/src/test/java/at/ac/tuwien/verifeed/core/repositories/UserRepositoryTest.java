package at.ac.tuwien.verifeed.core.repositories;

import at.ac.tuwien.verifeed.core.entities.VerifeedUser;
import at.ac.tuwien.verifeed.core.enums.UserRole;
import at.ac.tuwien.verifeed.core.util.DatabaseInitializer;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(initializers = {DatabaseInitializer.class})
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    @Transactional
    public void testStoreUser() {
        // given
        VerifeedUser user = createRandomUser();

        // when
        VerifeedUser storedUser = userRepository.save(user);

        // then
        assertUsers(storedUser, user);
    }

    @Test
    @Transactional
    public void testGetUserByEmail() {
        // given
        VerifeedUser user = createRandomUser();
        userRepository.save(user);

        // when
        Optional<VerifeedUser> storedUser = userRepository.findByEmail(user.getEmail());

        // then
        assertTrue(storedUser.isPresent());
        assertUsers(storedUser.get(), user);
    }

    /*@Test
    @Transactional
    public void testEnableUser() {
        // given
        VerifeedUser user = createRandomUser();
        user.setEnabled(false);
        VerifeedUser a = userRepository.save(user);

        // when
        userRepository.enableUserByEMail(user.getEmail());

        // then
        Optional<VerifeedUser> storedUser = userRepository.findByEmail(user.getEmail());
        assertTrue(storedUser.isPresent());
        assertTrue(user.isEnabled());
    }*/

    @Test
    @Transactional
    public void testDeleteUser() {
        // given
        VerifeedUser user = createRandomUser();
        userRepository.save(user);
        Optional<VerifeedUser> storedUser = userRepository.findByEmail(user.getEmail());
        assertTrue(storedUser.isPresent());

        // when
        userRepository.deleteById(storedUser.get().getId());

        // then
        Optional<VerifeedUser> deletedUser = userRepository.findByEmail(user.getEmail());
        assertFalse(deletedUser.isPresent());
    }

    private VerifeedUser createRandomUser() {
        VerifeedUser user = new VerifeedUser();
        String password = UUID.randomUUID().toString();
        String username = UUID.randomUUID().toString();
        String email = "test@example.com";
        user.setPassword(password);
        user.setUserRole(UserRole.ADMIN);
        user.setEmail(email);
        user.setUsername(username);
        user.setLocked(false);
        user.setEnabled(true);
        return user;
    }

    private void assertUsers(VerifeedUser user1,VerifeedUser user2) {
        assertNotNull(user1);
        assertNotNull(user1.getId());
        assertEquals(user1.getUserRole(), user2.getUserRole());
        assertEquals(user1.getUsername(), user2.getUsername());
        assertEquals(user1.getEmail(), user2.getEmail());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertFalse(user1.isLocked());
        assertTrue(user1.isEnabled());
    }

}
