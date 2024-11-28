package at.ac.tuwien.verifeed.core.security;

import at.ac.tuwien.verifeed.core.config.properties.SecurityProperties;
import at.ac.tuwien.verifeed.core.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserService userService;

    private final SecurityProperties securityProperties;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService, SecurityProperties securityProperties) {
        super(authenticationManager);
        this.userService = userService;
        this.securityProperties = securityProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        try {
            UsernamePasswordAuthenticationToken authToken = getAuthToken(request);
            if (authToken != null) {
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (IllegalArgumentException | JwtException e) {
            LOGGER.debug("Invalid authorization attempt: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid authorization header or token");
            return;
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthToken(HttpServletRequest request)
        throws JwtException, IllegalArgumentException {
        String token = request.getHeader(securityProperties.getAuth().getHeader());
        if (token == null || token.isEmpty()) {
            return null;
        }

        if (!token.startsWith(securityProperties.getAuth().getPrefix())) {
            throw new IllegalArgumentException("Authorization header is malformed or missing");
        }

        byte[] signingKey = securityProperties.getJwt().getSecret().getBytes();
        SecretKey secretKey = new SecretKeySpec(signingKey, "HmacSHA512");

        if (!token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token must start with 'Bearer'");
        }
        Claims claims = Jwts
                .parser()
                    .verifyWith(secretKey)
                    .build()
                .parseSignedClaims(token.replace(securityProperties.getAuth().getPrefix(), ""))
                .getPayload();

        String username = (String) claims.get("username");

        userService.loadUserByUsername(username);

        List<SimpleGrantedAuthority> authorities = ((List<?>) claims
            .get("rol")).stream()
            .map(authority -> new SimpleGrantedAuthority((String) authority))
            .toList();

        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Token contains no user");
        }

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
