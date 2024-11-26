package at.ac.tuwien.verifeed.core.security;

import at.ac.tuwien.verifeed.core.entities.Journalist;
import at.ac.tuwien.verifeed.core.service.JournalistService;
import at.ac.tuwien.verifeed.core.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserFilterBean {

    private final UserService userService;

    private final JournalistService journalistService;

    public UserFilterBean(UserService userService, JournalistService journalistService) {
        this.userService = userService;
        this.journalistService = journalistService;
    }

    public boolean targetedUserIdEqualsLoggedInUserId() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (servletRequestAttributes == null) {
            return false;
        }

        HttpServletRequest request = servletRequestAttributes.getRequest();
        String targetedId = extractTargetedUserId(request);
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = this.userService.getUserIdByEmail(principal);

        return Objects.equals(userId, targetedId);
    }

    public boolean isVerified() {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Journalist journalist = journalistService.getJournalistById(UUID.fromString(userService.getUserIdByEmail(principal)));
        return journalist.isVerified();
    }

    private String extractTargetedUserId(HttpServletRequest request) {
        String path = request.getRequestURI();
        Pattern userIdPattern = Pattern.compile("/user/([a-fA-F0-9\\-]{36})");
        Matcher matcher = userIdPattern.matcher(path);

        if (matcher.find()) {
            return matcher.group(1);
        }

        throw new IllegalArgumentException("Invalid URI for extracting user ID: " + path);
    }
}
