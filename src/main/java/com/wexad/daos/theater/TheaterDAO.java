package com.wexad.daos.theater;

import com.wexad.daos.BaseDAO;
import com.wexad.domains.theater.Theater;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class TheaterDAO extends BaseDAO<Theater, UUID> {
    protected TheaterDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private final RowMapper<Theater> rowMapper = (rs, numRow) -> Theater.builder()
            .id(UUID.fromString(rs.getString("id")))
            .name(rs.getString("name"))
            .location(rs.getString("location"))
            .isActive(rs.getBoolean("isActive"))
            .createdDate(rs.getTimestamp("createdDate").toLocalDateTime())
            .build();

    @Override
    public UUID save(Theater entity) {
        String sql = "select save_theater(?, ?)";
        String id = jdbcTemplate.queryForObject(sql, String.class, entity.getName(), entity.getLocation());
        return Objects.nonNull(id) ? UUID.fromString(id) : null;
    }

    @Override
    public void update(Theater entity) {
        String sql = "UPDATE theater SET name=? AND location=? where id = ?";
        jdbcTemplate.update(sql, entity.getName(), entity.getLocation(), entity.getId());
    }

    @Override
    public void delete(UUID uuid) {
        String sql = "UPDATE theater SET isActive = false where id = ?";
        jdbcTemplate.update(sql, uuid);
    }

    @Override
    public Theater findById(UUID uuid) {
        String sql = "select * from theater where id = ? and isActive = true";
        return jdbcTemplate.queryForObject(sql, rowMapper, uuid);
    }

    @Override
    public List<Theater> findAll() {
        return jdbcTemplate.query("select * from theater", rowMapper);
    }
}
