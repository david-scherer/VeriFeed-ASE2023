package at.ac.tuwien.verifeed.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface BackendException {
    public HttpStatus getStatusCode();
}
