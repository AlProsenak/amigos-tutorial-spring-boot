package dev.professional_fullstack_developer.tutorial.adapter.repository;

import dev.professional_fullstack_developer.tutorial.domain.entity.User;
import dev.professional_fullstack_developer.tutorial.domain.exception.BadRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserStaticRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    private long counter = 0;

    public UserStaticRepository() {
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
    public Optional<User> findByUsername(String username) {
        return this.users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return this.users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public User save(User user) {
        validateUser(user);
        user.setId(generateId());
        this.users.add(user);
        return user;
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            this.validateUser(entity);
            result.add(entity);
            this.saveAll(result);
        }
        return result;
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
    public void delete(User user) {
        long id = user.getId();
        Optional<User> deletedUser = this.users.stream()
                .filter(existingUser -> existingUser.getId() == id)
                .findFirst();

        deletedUser.ifPresent(this.users::remove);
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.users.stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.users.stream().
                anyMatch(user -> user.getEmail().equals(email));
    }

}
