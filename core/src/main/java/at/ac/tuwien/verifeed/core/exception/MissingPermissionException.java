package at.ac.tuwien.verifeed.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class MissingPermissionException extends RuntimeException implements BackendException {

    public MissingPermissionException(String message) {
        super(message, null);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.FORBIDDEN;
    }
}