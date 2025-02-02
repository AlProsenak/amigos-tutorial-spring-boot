package dev.professional_fullstack_developer.tutorial.adapter.repository.rowmapper;

import dev.professional_fullstack_developer.tutorial.domain.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = User.getInstance();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setBirthdate(rs.getDate("birthdate").toLocalDate());
        user.setCreatedAt(rs.getTimestamp("created_at").toInstant());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
        return user;
    }

}
