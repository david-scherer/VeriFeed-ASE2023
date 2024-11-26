package at.ac.tuwien.verifeed.core.config;

import at.ac.tuwien.verifeed.core.config.properties.SecurityProperties;
import at.ac.tuwien.verifeed.core.security.*;
import at.ac.tuwien.verifeed.core.service.JournalistService;
import at.ac.tuwien.verifeed.core.service.PostService;
import at.ac.tuwien.verifeed.core.service.UserService;
import at.ac.tuwien.verifeed.core.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final SecurityProperties securityProperties;

    private final UserService userService;

    private final JournalistService journalistService;

    private final PostService postService;

    private final JwtTokenizer jwtTokenizer;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder, SecurityProperties securityProperties, UserService userService, JournalistService journalistService, PostService postService, JwtTokenizer jwtTokenizer) {
        this.passwordEncoder = passwordEncoder;
        this.securityProperties = securityProperties;
        this.userService = userService;
        this.journalistService = journalistService;
        this.postService = postService;
        this.jwtTokenizer = jwtTokenizer;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/v*/registration/**", "/api/v*/login", "/api/v1/post/collection/*", "/api/v1/post", "/api/v1/post/{id}/commentaries")
                        .permitAll()
                        .requestMatchers(HttpMethod.OPTIONS)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .addFilter(new JwtAuthenticationFilter(authenticationManager, securityProperties, jwtTokenizer))
                .addFilter(new JwtAuthorizationFilter(authenticationManager, userService, securityProperties));
        return http.build();
    }

    @Bean
    public AuthenticationManager authBean(HttpSecurity http, UserServiceImpl userService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider(userService));
        return authenticationManagerBuilder.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserServiceImpl userService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public UserFilterBean userFilterBean() {
        return new UserFilterBean(userService, journalistService);
    }

    @Bean
    public PostFilterBean postFilterBean() {
        return new PostFilterBean(userService, postService);
    }

}
