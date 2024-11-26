package at.ac.tuwien.verifeed.core.config;

import at.ac.tuwien.verifeed.core.exception.ScrapingServiceErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScrapingServiceFeignConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new ScrapingServiceErrorDecoder();
    }
}
