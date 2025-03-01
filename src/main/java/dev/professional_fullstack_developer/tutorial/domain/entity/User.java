package dev.professional_fullstack_developer.tutorial.domain.entity;

import dev.professional_fullstack_developer.tutorial.domain.dto.CreateUserRequest;
import dev.professional_fullstack_developer.tutorial.domain.dto.UpdateUserRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;
import java.time.LocalDate;

@Entity(
        name = "end_user"
)
@Table(
        name = "end_user",
        schema = "tutorial",
        uniqueConstraints = {
                @UniqueConstraint(name = "end_user_username", columnNames = "username"),
                @UniqueConstraint(name = "end_user_email_key", columnNames = "email")
        }
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
            sequenceName = "end_user_id_sequence",
            schema = "tutorial",
            allocationSize = 1
    )
    @Column(nullable = false, unique = true)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private LocalDate birthdate;
    @Column(nullable = false)
    private Instant createdAt;
    @Column(nullable = false)
    private Instant updatedAt;

    protected User() {
    }

    private User(String username, String email, LocalDate birthdate) {
        this.username = username;
        this.email = email;
        this.birthdate = birthdate;
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthdate() {
        return this.birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static User getInstance() {
        return new User();
    }

    public static User from(CreateUserRequest userDto) {
        return new User(userDto.username(), userDto.email(), userDto.birthdate());
    }

    public void accept(UpdateUserRequest userDto) {
        this.username = userDto.username();
        this.email = userDto.email();
        this.birthdate = userDto.birthdate();
        this.updatedAt = Instant.now();
    }

}
