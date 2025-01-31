package dev.professional_fullstack_developer.tutorial.adapter.rest;

import dev.professional_fullstack_developer.tutorial.domain.dto.SimpleResponse;
import dev.professional_fullstack_developer.tutorial.domain.exception.BadRequestException;
import dev.professional_fullstack_developer.tutorial.domain.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(exception = {
            NotFoundException.class,
            BadRequestException.class
    })
    public Object handleNotFoundException(ResponseStatusException ex) {
        return new ResponseEntity<>(new SimpleResponse(ex.getReason()), ex.getStatusCode());
    }

}
