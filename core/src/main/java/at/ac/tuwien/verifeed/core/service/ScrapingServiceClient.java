package at.ac.tuwien.verifeed.core.service;

import at.ac.tuwien.verifeed.core.config.ScrapingServiceFeignConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "scraping",
        url = "${spring.cloud.openfeign.client.config.scraping.url}",
        configuration = ScrapingServiceFeignConfig.class
)
public interface ScrapingServiceClient {

    @Setter
    @Getter
    class ScrapeUrlRequest {
        private String url;

    }

    @PostMapping(value = "/scrape", produces = "application/json")
    String scrapePostingWithUrl(@RequestBody ScrapeUrlRequest urlRequest);

}
