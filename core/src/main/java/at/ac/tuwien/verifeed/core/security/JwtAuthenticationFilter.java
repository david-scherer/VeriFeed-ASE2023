package at.ac.tuwien.verifeed.core.security;

import at.ac.tuwien.verifeed.core.config.properties.SecurityProperties;
import at.ac.tuwien.verifeed.core.dto.LoginCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, SecurityProperties securityProperties,
                                   JwtTokenizer jwtTokenizer) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
        setFilterProcessesUrl(securityProperties.getAuth().getLoginUri());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {
        LoginCredentials user = null;
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), LoginCredentials.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()));
        } catch (IOException e) {
            throw new BadCredentialsException("Wrong API request or JSON schema", e);
        } catch (BadCredentialsException e) {
            if (user != null && user.getEmail() != null) {
                LOGGER.error("Unsuccessful authentication attempt for user with email {}", user.getEmail());
            }
            throw e;
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(failed.getMessage());
        LOGGER.debug("Invalid authentication attempt: {}", failed.getMessage());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        VerifeedUserDetails user = ((VerifeedUserDetails) authResult.getPrincipal());

        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        response.getWriter().write(jwtTokenizer.getAuthToken(user.getUsername(), user.getId(), roles));
        LOGGER.info("Successfully authenticated user with email {}", user.getUsername());
    }
}
