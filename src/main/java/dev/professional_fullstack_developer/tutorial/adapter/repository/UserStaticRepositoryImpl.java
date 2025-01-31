package dev.professional_fullstack_developer.tutorial.adapter.repository;

import dev.professional_fullstack_developer.tutorial.domain.entity.User;
import dev.professional_fullstack_developer.tutorial.domain.exception.BadRequestException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public  class UserStaticRepositoryImpl implements UserStaticRepository {

    private final List<User> users = new ArrayList<>();

    private long counter = 0;

    public UserStaticRepositoryImpl() {
        this.users.add(save(new User("Alex", "alex@gmail.com", LocalDate.of(2002, 1, 12))));
        this.users.add(save(new User("Alice", "alice@gmail.com", LocalDate.of(1998, 3, 24))));
        this.users.add(save(new User("Bob", "bob@gmail.com", LocalDate.of(2004, 7, 16))));
        this.users.add(save(new User("Robert", "robert@gmail.com", LocalDate.of(1980, 4, 28))));
        this.users.add(save(new User("Denise", "denise@gmail.com", LocalDate.of(2001, 12, 30))));
    }

    private long generateId() {
        this.counter += 1;
        return this.counter;
    }

    @Override
    public List<User> findAll() {
        return this.users;
    }

    @Override
    public Optional<User> findById(long id) {
        return this.users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    @Override
    public User save(User user) {
        validateUser(user);
        user.setId(generateId());
        this.users.add(user);
        return user;
    }

    public void validateUser(User user) {
        for (User persistedUser : users) {
            if (persistedUser.getEmail().equals(user.getEmail())) {
                throw new BadRequestException("User with email '%s' already exists".formatted(user.getEmail()));
            }
            if (persistedUser.getUsername().equals(user.getUsername())) {
               throw new BadRequestException("User with username '%s' already exists".formatted(user.getUsername()));
            }
        }
    }

    @Override
    public Optional<User> delete(User user) {
        long id = user.getId();
        Optional<User> deletedUser = this.users.stream()
                .filter(existingUser -> existingUser.getId() == id)
                .findFirst();

        deletedUser.ifPresent(this.users::remove);

        return deletedUser;
    }

}
