package dev.professional_fullstack_developer.tutorial.adapter.repository;

import dev.professional_fullstack_developer.tutorial.testcontainer.PostgresTestcontainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserJdbcTemplateRepositoryTest extends PostgresTestcontainer {

    private UserJdbcTemplateRepository testSubject;

    @BeforeEach
    void setUp() {
        this.testSubject = new UserJdbcTemplateRepository(getJdbcTemplate());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByUsername() {
    }

    @Test
    void findByEmail() {
    }

    @Test
    void save() {
    }

    @Test
    void saveAll() {
    }

    @Test
    void delete() {
    }

    @Test
    void existsByUsername() {
    }

    @Test
    void existsByEmail() {
    }

}