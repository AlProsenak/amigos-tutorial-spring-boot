package dev.professional_fullstack_developer.tutorial.application.local;

import com.github.javafaker.Faker;
import dev.professional_fullstack_developer.tutorial.adapter.repository.UserRepository;
import dev.professional_fullstack_developer.tutorial.domain.dto.CreateUserRequest;
import dev.professional_fullstack_developer.tutorial.domain.entity.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Profile({"static", "local"})
public final class TestDataInitializer {

    private static final Faker faker = new Faker();

    private final UserRepository userRepository;

    private TestDataInitializer(@Qualifier("userRepositoryBean") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    private void initializeData() {
        if (userRepository.findAll().isEmpty()) {
            List<User> users = getStaticTestUsers();

            for (int i = 0; i < 5; i++) {
                users.add(createFakeUser());
            }

            userRepository.saveAll(users);
        }
    }

    public static List<User> getStaticTestUsers() {
        List<User> users = new ArrayList<>();
        users.add(User.from(new CreateUserRequest("Alex", "alex@gmail.com", LocalDate.of(2002, 1, 12))));
        users.add(User.from(new CreateUserRequest("Alice", "alice@gmail.com", LocalDate.of(1998, 3, 24))));
        users.add(User.from(new CreateUserRequest("Bob", "bob@gmail.com", LocalDate.of(2004, 7, 16))));
        users.add(User.from(new CreateUserRequest("Robert", "robert@gmail.com", LocalDate.of(1980, 4, 28))));
        users.add(User.from(new CreateUserRequest("Denise", "denise@gmail.com", LocalDate.of(2001, 12, 30))));
        return users;
    }

    public static User createFakeUser() {
        CreateUserRequest userDto = new CreateUserRequest(
                faker.name().username(),
                faker.internet().emailAddress(),
                faker.date().past(80 * 365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        );
        return User.from(userDto);
    }

}
