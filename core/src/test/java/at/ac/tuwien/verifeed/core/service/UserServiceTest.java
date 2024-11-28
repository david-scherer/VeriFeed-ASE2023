package at.ac.tuwien.verifeed.core.service;

import at.ac.tuwien.verifeed.core.entities.ConfirmationToken;
import at.ac.tuwien.verifeed.core.entities.VerifeedUser;
import at.ac.tuwien.verifeed.core.enums.UserRole;
import at.ac.tuwien.verifeed.core.repositories.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private UserService userService;

    @Test
    public void loadUserByUsername_UserFound_ReturnsUserDetails() {
        // given
        VerifeedUser testUser = createRandomUser();

        // when
        Mockito.when(userRepository.findByEmail(testUser.getEmail()))
                .thenReturn(Optional.of(testUser));
        UserDetails userDetails = userService.loadUserByUsername(testUser.getEmail());

        // then
        assertNotNull(userDetails);
        assertEquals(testUser.getUsername(), userDetails.getUsername());
    }

    @Test
    public void loadUserByUsername_UserNotFound_ThrowsUsernameNotFoundException() {
        // given
        String userEmail = "nonexistent@example.com";

        // then
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(userEmail));
    }

    @Test
    public void signUpUser_NewUser_SuccessfulRegistration() {
        // given
        VerifeedUser newUser = createRandomUser();
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(newUser.getPassword())).thenReturn("encodedPassword");

        // when
        String token = userService.signUpUser(newUser);

        // then
        assertNotNull(token);
        Mockito.verify(userRepository, Mockito.times(1)).save(any(VerifeedUser.class));
        Mockito.verify(confirmationTokenService, Mockito.times(1)).saveConfirmationToken(any(ConfirmationToken.class));
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
 }
