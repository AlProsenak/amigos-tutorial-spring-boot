package dev.professional_fullstack_developer.tutorial.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {

    private static final HttpStatusCode status = HttpStatus.NOT_FOUND;
    private static final String defaultReason = "Not found";

    public NotFoundException() {
        super(status, defaultReason);

    }

    public NotFoundException(String reason) {
        super(status, reason);
    }

    public NotFoundException(String reason, Throwable cause) {
        super(status, reason, cause);
    }

}
