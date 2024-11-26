package at.ac.tuwien.verifeed.core.service;

import at.ac.tuwien.verifeed.core.dto.EditUserDto;
import at.ac.tuwien.verifeed.core.dto.ProfileImageDto;
import at.ac.tuwien.verifeed.core.dto.VerifeedUserDto;
import at.ac.tuwien.verifeed.core.entities.ConfirmationToken;
import at.ac.tuwien.verifeed.core.entities.VerifeedUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.UUID;

public interface UserService extends UserDetailsService {

    UserDetails loadUserByUsername(String username);

    String signUpUser(VerifeedUser appUser);

    int enableAppUser(String email);

    Optional<VerifeedUser> getUserByEmail(String email);

    Optional<VerifeedUser> getuserById(UUID uuid);

    void deleteUser(UUID id);

    String getUserIdByUsername(String principal);

    VerifeedUser updateUser(EditUserDto user, UUID id);

    String getUserIdByEmail(String principal);

    VerifeedUser setImage(ProfileImageDto imageDto, UUID uuid);
}
