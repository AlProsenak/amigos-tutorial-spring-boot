package dev.professional_fullstack_developer.tutorial;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

    private static Flyway flyway;
    private static MigrateResult migrationResult;

    @BeforeAll
    static void init() {
        flyway = Flyway.configure()
                .dataSource(container.getJdbcUrl(), container.getUsername(), container.getPassword())
                .load();

        migrationResult = flyway.migrate();
    }

    @AfterAll
    static void cleanup() {
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
