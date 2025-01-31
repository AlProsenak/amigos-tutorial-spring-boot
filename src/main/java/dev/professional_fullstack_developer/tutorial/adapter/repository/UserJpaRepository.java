package dev.professional_fullstack_developer.tutorial.adapter.repository;

import dev.professional_fullstack_developer.tutorial.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Semantic annotation is not required for interfaces extended by JPARepository,
// since by default it auto-instantiates the bean, via @EnableJpaRepositories declared in JpaRepositoriesRegistrar.
@Repository
public interface UserJpaRepository extends JpaRepository<User, Long>, UserRepository {
}
