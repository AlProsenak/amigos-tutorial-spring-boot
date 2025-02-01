package dev.professional_fullstack_developer.tutorial.adapter.repository;

import dev.professional_fullstack_developer.tutorial.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();

    Optional<User> findById(long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User save(User user);

    <S extends User> List<S> saveAll(Iterable<S> entities);

    void delete(User user);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
