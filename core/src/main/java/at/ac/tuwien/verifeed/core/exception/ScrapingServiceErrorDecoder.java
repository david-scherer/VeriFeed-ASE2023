package at.ac.tuwien.verifeed.core.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

public class ScrapingServiceErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new ScrapingServiceException("The url was not valid.");
            case 422 -> new ScrapingServiceException("We are not supporting this platform yet.");
            case 404 -> new EntityNotFoundException("The post couldn't be found.");
            default -> new ScrapingServiceException("Sorry we had some troubles on our end. Please try again later.");
        };
    }
}
