package at.ac.tuwien.verifeed.core.service.impl;

import at.ac.tuwien.verifeed.core.dto.EditUserDto;
import at.ac.tuwien.verifeed.core.dto.ProfileImageDto;
import at.ac.tuwien.verifeed.core.dto.VerifeedUserDto;
import at.ac.tuwien.verifeed.core.entities.ConfirmationToken;
import at.ac.tuwien.verifeed.core.entities.VerifeedUser;
import at.ac.tuwien.verifeed.core.exception.EntityNotFoundException;
import at.ac.tuwien.verifeed.core.exception.RegistrationException;
import at.ac.tuwien.verifeed.core.mapper.impl.VerifeedUserMapperImpl;
import at.ac.tuwien.verifeed.core.repositories.UserRepository;
import at.ac.tuwien.verifeed.core.security.VerifeedUserDetails;
import at.ac.tuwien.verifeed.core.service.ConfirmationTokenService;
import at.ac.tuwien.verifeed.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           ConfirmationTokenServiceImpl confirmationTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<VerifeedUser> userOptional = getUserByEmail(username);

        if (userOptional.isPresent()) {

            VerifeedUser user = userOptional.get();

            List<GrantedAuthority> grantedAuthorities;

            grantedAuthorities = switch (user.getUserRole()) {
                case ADMIN -> AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_JOURNALIST", "ROLE_USER");
                case JOURNALIST -> AuthorityUtils.createAuthorityList( "ROLE_JOURNALIST", "ROLE_USER");
                case USER -> AuthorityUtils.createAuthorityList("ROLE_USER");
            };

            return new VerifeedUserDetails(user.getId(), user.getEmail(), user.getPassword(), grantedAuthorities);
        }

        throw new UsernameNotFoundException(String.format("User with username %s not found", username));
    }

    @Override
    public String signUpUser(VerifeedUser appUser) {
        boolean userExists = userRepository
                .findByEmail(appUser.getEmail())
                .isPresent();

        if (userExists) {
            throw new RegistrationException("This E-Mail address is already in use. Please choose another address.");
        }

        String encodedPassword = passwordEncoder
                .encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        userRepository.save(appUser);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(
                confirmationToken);

        return token;
    }

    @Override
    public int enableAppUser(String email) {
        return userRepository.enableUserByEMail(email);
    }

    @Override
    public Optional<VerifeedUser> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<VerifeedUser> getuserById(UUID uuid) {
        return userRepository.findById(uuid);
    }

    @Override
    public void deleteUser(UUID id) {
        var userToDelete = getuserById(id);

        if (userToDelete.isPresent()) {
            this.confirmationTokenService.deleteTokenFor(userToDelete.get());
            this.userRepository.deleteById(id);
            return;
        }

        throw new EntityNotFoundException("Cannot delete user. It does not exist.");
    }

    @Override
    public String getUserIdByUsername(String username) {

        Optional<VerifeedUser> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get().getId().toString();
        }

        throw new UsernameNotFoundException(String.format("user with username %s not found", username));
    }

    @Override
    public String getUserIdByEmail(String email) {

        Optional<VerifeedUser> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get().getId().toString();
        }

        throw new UsernameNotFoundException(String.format("user with email %s not found", email));
    }

    @Override
    public VerifeedUser setImage(ProfileImageDto imageDto, UUID uuid) {
        Optional<VerifeedUser> userOptional = getuserById(uuid);

        if (userOptional.isPresent()) {
            VerifeedUser user = userOptional.get();
            user.setImg(imageDto.getImg());

            return userRepository.save(user);
        }

        throw new EntityNotFoundException(String.format("user with id %s not found", uuid.toString()));
    }

    @Override
    public VerifeedUser updateUser(EditUserDto user, UUID id) {
        Optional<VerifeedUser> userOptional = getuserById(id);
        if (userOptional.isPresent()) {
            VerifeedUser updatedUser = userOptional.get();
            updatedUser.setUsername(user.getUsername());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder
                        .encode(user.getPassword());
                updatedUser.setPassword(encodedPassword);
            }
            userRepository.save(updatedUser);
            return updatedUser;
        }
        throw new EntityNotFoundException("User with id " + id + " does not exist.");
    }
}
