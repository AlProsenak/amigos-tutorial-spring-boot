package dev.professional_fullstack_developer.tutorial.adapter.repository;

import dev.professional_fullstack_developer.tutorial.domain.entity.User;
import dev.professional_fullstack_developer.tutorial.testcontainer.PostgresTestcontainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static dev.professional_fullstack_developer.tutorial.application.local.TestDataInitializer.getStaticTestUsers;
import static org.assertj.core.api.Assertions.assertThat;

// Instantiates JPA relevant beans only, which is a lot less than using @SpringBootTest.
@DataJpaTest
// Prevents initializing tests with embedded database. Requires explicit Testcontainer configuration for it to work.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserJpaRepositoryTest extends PostgresTestcontainer {

    private final UserRepository testSubject;

    private List<User> users;

    @Autowired
    public UserJpaRepositoryTest(UserJpaRepository repository) {
        this.testSubject = repository;
    }

    @BeforeEach
    void setUp() {
        this.users = getStaticTestUsers();
        testSubject.saveAll(this.users);
    }

    @AfterEach
    void teardown() {
        this.testSubject.deleteAll();
    }

    @Test
    void findByUsername() {
        User expectedUser = users.getFirst();
        String expected = expectedUser.getUsername();

        Optional<User> actualUser = testSubject.findByUsername(expected);

        assertThat(actualUser).isPresent().hasValueSatisfying(user ->
                assertThat(user.getUsername()).isEqualTo(expected)
        );
    }

    @Test
    void findByEmail() {
        User expectedUser = users.getFirst();
        String expected = expectedUser.getEmail();

        Optional<User> actualUser = testSubject.findByEmail(expected);

        assertThat(actualUser).isPresent().hasValueSatisfying(user ->
                assertThat(user.getEmail()).isEqualTo(expected)
        );
    }

}
