package dev.professional_fullstack_developer.tutorial;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class TestcontainersTest {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:alpine")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres");

    @Test
    void canStartPostgresDatabase() {
        assertThat(container.isCreated()).isTrue();
        assertThat(container.isRunning()).isTrue();
        // TODO: Fix error:
        //  java.lang.RuntimeException: This container's image does not have a healthcheck declared, so health cannot be determined. Either amend the image or use another approach to determine whether containers are healthy.
//        assertThat(container.isHealthy()).isTrue();
    }

}
