package dev.professional_fullstack_developer.tutorial.adapter.repository;

import dev.professional_fullstack_developer.tutorial.adapter.repository.rowmapper.UserRowMapper;
import dev.professional_fullstack_developer.tutorial.domain.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class UserJdbcTemplateRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String USER_SELECT_ALL_SQL = """
            SELECT * FROM tutorial.end_user;
            """;

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
                USER_SELECT_ALL_SQL,
                new UserRowMapper()
        );
    }

    private static final String USER_SELECT_BY_ID_SQL = """
            SELECT * FROM tutorial.end_user WHERE id = ?;
            """;

    @Override
    public Optional<User> findById(long id) {
        return jdbcTemplate.query(
                USER_SELECT_BY_ID_SQL,
                new UserRowMapper(),
                id
        ).stream().findFirst();
    }

    private static final String USER_SELECT_BY_USERNAME_SQL = """
            SELECT * FROM tutorial.end_user WHERE username = ?;
            """;

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query(
                USER_SELECT_BY_USERNAME_SQL,
                new UserRowMapper(),
                username
        ).stream().findFirst();
    }

    private static final String USER_SELECT_BY_EMAIL_SQL = """
            SELECT * FROM tutorial.end_user WHERE email = ?;
            """;

    @Override
    public Optional<User> findByEmail(String email) {
        return jdbcTemplate.query(
                USER_SELECT_BY_EMAIL_SQL,
                new UserRowMapper(),
                email
        ).stream().findFirst();
    }

    private static final String USER_UPSERT_SQL = """
            WITH updated AS (
                -- Attempt to update if :id is provided
                UPDATE tutorial.end_user
                    SET
                        username = ?,
                        email = ?,
                        birthdate = ?,
                        updated_at = ?
                    WHERE id = ?
                    RETURNING *
            ),
                 inserted AS (
                     -- If the UPDATE did not affect any rows, perform an INSERT
                     INSERT INTO tutorial.end_user (username, email, birthdate, created_at, updated_at)
                         SELECT ?, ?, ?, ?, ?
                         WHERE NOT EXISTS (SELECT 1 FROM updated)
                         RETURNING *
                 )
            -- Return the result of either the UPDATE or the INSERT
            SELECT * FROM updated
            UNION ALL
            SELECT * FROM inserted;
            """;

    @Override
    public User save(User user) {
        Timestamp now = Timestamp.from(Instant.now());
        return jdbcTemplate.queryForObject(
                USER_UPSERT_SQL,
                new UserRowMapper(),
                user.getUsername(), user.getEmail(), user.getBirthdate(), now, user.getId(),
                user.getUsername(), user.getEmail(), user.getBirthdate(), now, now
        );
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> users) {
        Collection<S> usersCollection = StreamSupport.stream(users.spliterator(), false).toList();
        int[][] updateCount = jdbcTemplate.batchUpdate(
                USER_UPSERT_SQL,
                usersCollection,
                usersCollection.size(),
                (PreparedStatement ps, S user) -> {
                    Timestamp now = Timestamp.from(Instant.now());
                    // update
                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getEmail());
                    ps.setDate(3, Date.valueOf(user.getBirthdate()));
                    ps.setTimestamp(4, now);
                    // Using setObject() instead of setLong(), since ID can be null.
                    ps.setObject(5, user.getId());

                    // insert
                    ps.setString(6, user.getUsername());
                    ps.setString(7, user.getEmail());
                    ps.setDate(8, Date.valueOf(user.getBirthdate()));
                    ps.setTimestamp(9, now);
                    ps.setTimestamp(10, now);
                }
        );

        // TODO: figure out implementation for returning saved users
        return List.of();
    }

    private static final String USER_DELETE_SQL = """
            DELETE FROM tutorial.end_user
            WHERE id = ?;
            """;

    @Override
    public void delete(User user) {
        jdbcTemplate.update(USER_DELETE_SQL, user.getId());
    }

    private static final String USER_EXISTS_BY_USERNAME_SQL = """
            SELECT EXISTS(SELECT 1 FROM tutorial.end_user WHERE username = ?);
            """;

    @Override
    public boolean existsByUsername(String username) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                USER_EXISTS_BY_USERNAME_SQL,
                (ResultSet rs, int rowNumber) -> rs.getBoolean(1),
                username
        ));
    }

    private static final String USER_EXISTS_BY_EMAIL_SQL = """
            SELECT EXISTS(SELECT 1 FROM tutorial.end_user WHERE username = ?);
            """;

    @Override
    public boolean existsByEmail(String email) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                USER_EXISTS_BY_EMAIL_SQL,
                (ResultSet rs, int rowNumber) -> rs.getBoolean(1),
                email
        ));
    }

}
