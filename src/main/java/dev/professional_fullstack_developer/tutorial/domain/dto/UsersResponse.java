package dev.professional_fullstack_developer.tutorial.domain.dto;

import dev.professional_fullstack_developer.tutorial.domain.entity.User;

import java.util.List;

public record UsersResponse(List<User> users) {
}
