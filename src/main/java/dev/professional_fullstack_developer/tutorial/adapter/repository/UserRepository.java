package dev.professional_fullstack_developer.tutorial.adapter.repository;

import dev.professional_fullstack_developer.tutorial.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> getUsers();

    Optional<User> getUserById(long id);

    long createUser(String name, String email);

    Optional<User> deleteUser(long id);

}
