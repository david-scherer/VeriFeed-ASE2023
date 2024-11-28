package at.ac.tuwien.verifeed.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ScrapingServiceException extends RuntimeException implements BackendException {

    public ScrapingServiceException(String message) {
        super(message, null);
    }


    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}