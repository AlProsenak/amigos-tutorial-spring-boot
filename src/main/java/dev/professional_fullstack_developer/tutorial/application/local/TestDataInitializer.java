package dev.professional_fullstack_developer.tutorial.application.local;

import dev.professional_fullstack_developer.tutorial.adapter.repository.UserRepository;
import dev.professional_fullstack_developer.tutorial.domain.entity.User;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile({"static", "local"})
public final class TestDataInitializer {

    private final UserRepository userRepository;

    private TestDataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    private void initializeData() {
        if (userRepository.findAll().isEmpty()) {
            userRepository.save(new User("Alex", "alex@gmail.com", LocalDate.of(2002, 1, 12)));
            userRepository.save(new User("Alice", "alice@gmail.com", LocalDate.of(1998, 3, 24)));
            userRepository.save(new User("Bob", "bob@gmail.com", LocalDate.of(2004, 7, 16)));
            userRepository.save(new User("Robert", "robert@gmail.com", LocalDate.of(1980, 4, 28)));
            userRepository.save(new User("Denise", "denise@gmail.com", LocalDate.of(2001, 12, 30)));
        }
    }

}
