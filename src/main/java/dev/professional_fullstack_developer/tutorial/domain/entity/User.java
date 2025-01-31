package dev.professional_fullstack_developer.tutorial.domain.entity;

import java.time.Instant;
import java.time.LocalDate;

public class User {

    private final long id;
    private final String username;
    private final String email;
    private final LocalDate birthdate;
    private final Instant createdAt;
    private final Instant updatedAt;

    public User(long id, String username, String email, LocalDate birthdate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.birthdate = birthdate;
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public LocalDate getBirthdate() {
        return this.birthdate;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

}
