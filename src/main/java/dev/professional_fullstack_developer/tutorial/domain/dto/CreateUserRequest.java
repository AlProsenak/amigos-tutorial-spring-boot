package dev.professional_fullstack_developer.tutorial.domain.dto;

import java.time.LocalDate;

public record CreateUserRequest(String username, String email, LocalDate birthdate) {
}
