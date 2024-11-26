package at.ac.tuwien.verifeed.core.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    @Value("${mail.host}")
    private String host;
    @Value("${mail.port}")
    private Integer port;

    @Bean
    @Profile("production")
    public JavaMailSender javaMailSenderProd() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("mail.gmx.net");
        mailSender.setPort(587);
        mailSender.setUsername("verifeed@gmx.at");
        mailSender.setPassword("Verifeed1234!");
        return mailSender;
    }

    @Bean
    @Profile("dev")
    public JavaMailSender javaMailSenderDev() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        return mailSender;
    }
}

