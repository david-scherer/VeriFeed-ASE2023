package at.ac.tuwien.verifeed.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Error", e);
        var statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        if (e instanceof BackendException) {
            statusCode = ((BackendException) e).getStatusCode();
        }
        if (e instanceof AccessDeniedException) {
            statusCode = HttpStatus.FORBIDDEN;
        }
        return ResponseEntity
                .status(statusCode)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(e.getMessage()));
    }
}
