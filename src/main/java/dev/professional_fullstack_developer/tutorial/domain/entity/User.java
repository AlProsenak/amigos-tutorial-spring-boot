package dev.professional_fullstack_developer.tutorial.domain.entity;

import dev.professional_fullstack_developer.tutorial.domain.dto.CreateUserRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

import java.time.Instant;
import java.time.LocalDate;

@Entity(
        name = "end_user"
)
public class User {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            // Reference to the sequence generator by its name.
            generator = "user_id_sequence_name"
    )
    @SequenceGenerator(
            // JPA reference name.
            name = "user_id_sequence_name",
            // Sequence Generator uses sequenceName to generate DB sequence name.
            sequenceName = "end_user_id_sequence"
    )
    private long id;
    private String username;
    private String email;
    private LocalDate birthdate;
    private Instant createdAt;
    private Instant updatedAt;

    protected User() {
    }

    public User(String username, String email, LocalDate birthdate) {
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

    public void setId(long id) {
        this.id = id;
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

    public static User from(CreateUserRequest userDto) {
        return new User(userDto.username(), userDto.email(), userDto.birthdate());
    }

}
