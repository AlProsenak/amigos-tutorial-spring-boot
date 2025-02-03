package dev.professional_fullstack_developer.tutorial;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
// Spins up whole application context, therefore it should not be used for unit tests or simple layer/component tests, where context can be self-configured.
//@SpringBootTest
public class TestcontainersTest {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:alpine")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres")
            .withExposedPorts(5432);

    private static Flyway flyway;
    private static MigrateResult migrationResult;

    // Programmatically registers configuration properties from container, for other dependent beans to use them.
    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @BeforeAll
    static void init() {
        flyway = Flyway.configure()
                .dataSource(container.getJdbcUrl(), container.getUsername(), container.getPassword())
                .load();

        migrationResult = flyway.migrate();
    }

    @AfterAll
    static void cleanup() {
        // Container technically already auto closes after finished tests.
        container.close();
    }

    @Test
    void canInitializePostgresDatabase() {
        assertThat(container.isCreated()).isTrue();
        assertThat(container.isRunning()).isTrue();
        // TODO: Fix error:
        //  java.lang.RuntimeException: This container's image does not have a healthcheck declared, so health cannot be determined. Either amend the image or use another approach to determine whether containers are healthy.
//        assertThat(container.isHealthy()).isTrue();
    }

    @Test
    void canApplyFlywayMigrations() {
        assertThat(migrationResult.success).isTrue();
    }

}
