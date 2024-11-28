package at.ac.tuwien.verifeed.core.controller;

import at.ac.tuwien.verifeed.core.dto.EditUserDto;
import at.ac.tuwien.verifeed.core.dto.ProfileImageDto;
import at.ac.tuwien.verifeed.core.dto.VerifeedUserDto;
import at.ac.tuwien.verifeed.core.entities.VerifeedUser;
import at.ac.tuwien.verifeed.core.exception.EntityNotFoundException;
import at.ac.tuwien.verifeed.core.mapper.impl.VerifeedUserMapperImpl;
import at.ac.tuwien.verifeed.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${web.api.base}/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final VerifeedUserMapperImpl verifeedUserMapper;

    @Autowired
    public UserController(UserService userService, VerifeedUserMapperImpl verifeedUserMapper) {
        this.userService = userService;
        this.verifeedUserMapper = verifeedUserMapper;
    }

    @GetMapping("/{id}")
    public VerifeedUserDto getUserById(@PathVariable("id") String id) {
        logger.info("User by Id {} Request", id);
        var user = userService.getuserById(UUID.fromString(id));
        if (user.isPresent()) {
            VerifeedUser actualUser = user.get();
            actualUser.setPassword(null);

            return verifeedUserMapper.entityToDto(actualUser);
        }
        throw new EntityNotFoundException("User with id " + id + " does not exist.");
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @userFilterBean.targetedUserIdEqualsLoggedInUserId())")
    @PutMapping("/{id}")
    public VerifeedUserDto updateUserById(@RequestBody EditUserDto user, @PathVariable("id") String id) {
        logger.info("Update user by Id {} Request", id);
        return verifeedUserMapper.entityToDto(this.userService.updateUser(user, UUID.fromString(id)));
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @userFilterBean.targetedUserIdEqualsLoggedInUserId())")
    @PutMapping("/{id}/img")
    public VerifeedUserDto uploadImageById(@RequestBody ProfileImageDto imageDto, @PathVariable("id") String id) {
        logger.info("Update user by Id {} Request", id);
        return verifeedUserMapper.entityToDto(this.userService.setImage(imageDto, UUID.fromString(id)));
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @userFilterBean.targetedUserIdEqualsLoggedInUserId())")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") String id) {
        logger.info("Delete user by Id {} Request", id);
        userService.deleteUser(UUID.fromString(id));
    }
}
