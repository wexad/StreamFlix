package com.wexad.daos.user;

import com.wexad.daos.BaseDAO;
import com.wexad.domains.user.AuthUser;
import lombok.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserDAO extends BaseDAO<AuthUser, UUID> {

    protected UserDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private final RowMapper<AuthUser> rowMapper = (rs, numRow) -> AuthUser.builder()
            .id(UUID.fromString(rs.getString("id")))
            .name(rs.getString("name"))
            .email(rs.getString("email"))
            .password(rs.getString("password"))
            .phoneNumber(rs.getString("phoneNumber"))
            .role(rs.getString("role"))
            .isActive(rs.getBoolean("isActive"))
            .createdDate(rs.getTimestamp("createdDate").toLocalDateTime())
            .build();

    @Override
    public UUID save(AuthUser entity) {
        String id = jdbcTemplate.queryForObject("SELECT save_user(?,?,?,?,?)",
                String.class,
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getPhoneNumber(), null);
        return Objects.nonNull(id) ? UUID.fromString(id) : null;
    }

    @Override
    public void update(AuthUser entity) {
        String sql = "UPDATE users SET name=? AND email=? AND password=? AND phoneNumber=? AND role=? WHERE id=?;";
        jdbcTemplate.update(sql, entity.getName(), entity.getEmail(), entity.getPassword(), entity.getPhoneNumber(), entity.getRole(), entity.getId().toString());
    }

    @Override
    public void delete(UUID uuid) {
        String sql = "UPDATE users SET isActive = false WHERE id=?;";
        jdbcTemplate.update(sql, uuid.toString());
    }

    @Override
    public AuthUser findById(UUID uuid) {
        String sql = "SELECT * FROM users WHERE id=?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, uuid.toString());
    }

    @Override
    public List<AuthUser> findAll() {
        String sql = "SELECT * FROM users;";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<AuthUser> findByEmail(String email) {
        AuthUser user = jdbcTemplate.queryForObject(
                "select * from users where isActive = true and email = ?;",
                rowMapper, email
        );
        if (user != null) {
            System.out.println(user.getId());
            System.out.println("User found: " + user); // Debug statement
        } else {
            System.out.println("No user found with email: " + email); // Debug statement
        }
        return Optional.ofNullable(user);
    }

    public AuthUser login(@NonNull String email, @NonNull String password) {
        var sql = "SELECT * FROM users WHERE is_active AND username=? AND password=?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, email, password);
    }

    public void makeAdmin(String  id) {
        String sql = "UPDATE users SET role = 'ADMIN' WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }

    public void blockUser(String id) {
        String sql = "UPDATE users SET isActive = false WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }
}
