package at.ac.tuwien.verifeed.core.controller;

import at.ac.tuwien.verifeed.core.dto.RegistrationRequest;
import at.ac.tuwien.verifeed.core.email.StaticHTMLProvider;
import at.ac.tuwien.verifeed.core.service.RegistrationService;
import at.ac.tuwien.verifeed.core.service.impl.RegistrationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("${web.api.base}/registration")
public class RegistrationController {

    private final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    @Value("${web.frontend.address}:${web.frontend.port}/login")
    private String loginAddress;

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationServiceImpl registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public void register(@RequestBody @Valid RegistrationRequest request) {
        registrationService.register(request);
    }

    @GetMapping(path = "/confirm", produces = MediaType.TEXT_HTML_VALUE)
    public String confirm(@RequestParam("token") String token) {
        registrationService.confirmToken(token);
        return StaticHTMLProvider.composeRedirectPage(this.loginAddress);
    }
}
