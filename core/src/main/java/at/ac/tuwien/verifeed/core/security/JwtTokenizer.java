package at.ac.tuwien.verifeed.core.security;

import at.ac.tuwien.verifeed.core.config.properties.SecurityProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class JwtTokenizer {

    private final SecurityProperties securityProperties;

    public JwtTokenizer(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public String getAuthToken(String username, UUID uuid, List<String> roles) {
        byte[] signingKey = securityProperties.getJwt().getSecret().getBytes();
        String token = Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(signingKey), Jwts.SIG.HS512)
            .header().add("typ", securityProperties.getJwt().getType()).and()
            .issuer(securityProperties.getJwt().getIssuer())
            .audience().add(securityProperties.getJwt().getAudience()).and()
            .subject(String.valueOf(uuid))
            .claim("username", username)
            .expiration(new Date(System.currentTimeMillis() + securityProperties.getJwt().getExpirationTime()))
            .claim("rol", roles)
            .compact();
        return securityProperties.getAuth().getPrefix() + token;
    }
}
