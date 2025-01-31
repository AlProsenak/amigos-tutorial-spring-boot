package dev.professional_fullstack_developer.tutorial.adapter.repository;

import dev.professional_fullstack_developer.tutorial.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserStaticRepository {

    Optional<User> findById(long id);

    List<User> findAll();

    User save(User user);

    Optional<User> delete(User user);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
