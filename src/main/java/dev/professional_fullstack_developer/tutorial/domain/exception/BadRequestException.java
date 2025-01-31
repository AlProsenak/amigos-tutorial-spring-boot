package dev.professional_fullstack_developer.tutorial.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestException extends ResponseStatusException {

    private static final HttpStatusCode status = HttpStatus.BAD_REQUEST;

    public BadRequestException(String reason) {
        super(status, reason);
    }

    public BadRequestException(String reason, Throwable cause) {
        super(status, reason, cause);
    }

}
