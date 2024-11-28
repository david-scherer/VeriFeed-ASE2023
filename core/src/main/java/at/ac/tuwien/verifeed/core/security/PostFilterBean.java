package at.ac.tuwien.verifeed.core.security;

import at.ac.tuwien.verifeed.core.entities.Commentary;
import at.ac.tuwien.verifeed.core.service.PostService;
import at.ac.tuwien.verifeed.core.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostFilterBean {

    private final UserService userService;

    private final PostService postService;

    @Autowired
    public PostFilterBean(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    public boolean targetedCommentaryPublishedByIdEqualsLoggedInUserId() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (servletRequestAttributes == null) {
            return false;
        }

        HttpServletRequest request = servletRequestAttributes.getRequest();
        String commentaryId = extractTargetedCommentaryId(request);

        Commentary commentary = postService.getComment(commentaryId);

        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = this.userService.getUserIdByEmail(principal);

        return Objects.equals(commentary.getPublishedBy().getId().toString(), userId);
    }

    private String extractTargetedCommentaryId(HttpServletRequest request) {
        String path = request.getRequestURI();
        Pattern userIdPattern = Pattern.compile("/commentaries/([a-fA-F0-9\\-]{36})");
        Matcher matcher = userIdPattern.matcher(path);

        if (matcher.find()) {
            return matcher.group(1);
        }

        throw new IllegalArgumentException("Invalid URI for extracting commentary ID: " + path);
    }
}
