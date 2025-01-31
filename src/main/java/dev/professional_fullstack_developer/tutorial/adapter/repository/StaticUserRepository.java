package dev.professional_fullstack_developer.tutorial.adapter.repository;

import dev.professional_fullstack_developer.tutorial.domain.dto.CreateUserRequest;
import dev.professional_fullstack_developer.tutorial.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public  class StaticUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    private long counter = 0;

    public StaticUserRepository() {
        this.users.add(new User(generateId(), "Alex", "alex@gmail.com", LocalDate.of(2002, 1, 12)));
        this.users.add(new User(generateId(), "Alice", "alice@gmail.com", LocalDate.of(1998, 3, 24)));
        this.users.add(new User(generateId(), "Bob", "bob@gmail.com", LocalDate.of(2004, 7, 16)));
        this.users.add(new User(generateId(), "Robert", "robert@gmail.com", LocalDate.of(1980, 4, 28)));
        this.users.add(new User(generateId(), "Denise", "denise@gmail.com", LocalDate.of(2001, 12, 30)));
    }

    private long generateId() {
        this.counter += 1;
        return this.counter;
    }

    @Override
    public List<User> getUsers() {
        return this.users;
    }

    @Override
    public Optional<User> getUserById(long id) {
        return this.users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    @Override
    public long createUser(CreateUserRequest userDto) {
        User user = new User(generateId(), userDto.username(), userDto.email(), userDto.birthdate());
        this.users.add(user);
        return user.getId();
    }

    @Override
    public Optional<User> deleteUser(long id) {
        Optional<User> deletedUser = this.users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();

        deletedUser.ifPresent(this.users::remove);

        return deletedUser;
    }

}
