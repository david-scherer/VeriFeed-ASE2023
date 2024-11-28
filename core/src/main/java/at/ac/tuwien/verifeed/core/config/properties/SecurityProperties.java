package at.ac.tuwien.verifeed.core.config.properties;

import at.ac.tuwien.verifeed.core.config.SecurityPropertiesConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
@AllArgsConstructor
public class SecurityProperties {
    private final SecurityPropertiesConfig.Auth auth;
    private final SecurityPropertiesConfig.Jwt jwt;
}
