package dev.professional_fullstack_developer.tutorial.adapter.repository;

import dev.professional_fullstack_developer.tutorial.domain.dto.CreateUserRequest;
import dev.professional_fullstack_developer.tutorial.domain.entity.User;
import dev.professional_fullstack_developer.tutorial.extension.DemoExtension;
import dev.professional_fullstack_developer.tutorial.testcontainer.PostgresTestcontainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static dev.professional_fullstack_developer.tutorial.application.local.TestDataInitializer.getStaticTestUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({DemoExtension.class})
class UserJdbcTemplateRepositoryTest extends PostgresTestcontainer {

    private UserJdbcTemplateRepository testSubject;

    private List<User> users;
    private int userCount;

    @BeforeEach
    void setUp() {
        this.testSubject = new UserJdbcTemplateRepository(getJdbcTemplate());

        // TODO: use migrations to insert test data
        this.users = getStaticTestUsers();
        this.testSubject.saveAll(this.users);
        this.userCount = this.users.size();
    }

    @AfterEach
    void tearDown() {
        // TODO: use migrations to teardown test data
        this.testSubject.deleteAll();
        this.userCount = 0;
        this.users = null;
    }

    @Test
    void findAll() {
        List<User> result = testSubject.findAll();

        assertEquals(userCount, result.size());
    }

    @Test
    void findById() {
        User beforeSavedUser = this.users.getFirst();
        Optional<User> expectedUser = testSubject.findByUsername(beforeSavedUser.getUsername());

        assertTrue(expectedUser.isPresent());
        long expectedId = expectedUser.get().getId();

        Optional<User> result = testSubject.findById(expectedId);

        assertThat(result).isPresent().hasValueSatisfying(user -> {
            assertEquals(user.getId(), expectedId);
        });
    }

    @Test
    void findByUsername() {
        String expectedUsername = "Alex";

        Optional<User> result = testSubject.findByUsername(expectedUsername);

        assertThat(result).isPresent().hasValueSatisfying(user -> {
            assertThat(user.getUsername()).isEqualTo(expectedUsername);
        });
    }

    @Test
    void findByEmail() {
        String expectedEmail = "alex@gmail.com";

        Optional<User> result = testSubject.findByEmail(expectedEmail);

        assertThat(result).isPresent().hasValueSatisfying(user -> {
            assertThat(user.getEmail()).isEqualTo(expectedEmail);
        });
    }

    @Test
    void save() {
        String username = "Tester";
        User expectedNewUser = User.from(new CreateUserRequest(username, "tester@gmail.com", LocalDate.of(2002, 1, 12)));

        User result = testSubject.save(expectedNewUser);

        Optional<User> savedUser = testSubject.findByUsername(username);

        assertThat(savedUser).isPresent().hasValueSatisfying(user -> {
            assertThat(user.getId()).isEqualTo(result.getId());
        });
    }

    @Test
    void saveAll() {
        // TODO: testSubject method is missing correct return result
    }

    @Test
    void delete() {
        Optional<User> expectedUser = testSubject.findById(1L);

        assertTrue(expectedUser.isPresent());

        testSubject.delete(expectedUser.get());

        Optional<User> result = testSubject.findById(expectedUser.get().getId());

        assertTrue(result.isEmpty());
    }

    @Test
    void existsByUsername() {
        boolean result = testSubject.existsByUsername("Alex");

        assertTrue(result);
    }

    @Test
    void existsByEmail() {
        User newUser = User.from(new CreateUserRequest("Tester", "tester@gmail.com", LocalDate.of(2002, 1, 12)));
        User createdUser = testSubject.save(newUser);

        boolean result = testSubject.existsByEmail(newUser.getEmail());

        assertTrue(result);
    }

}