package com.wexad.daos.screen;

import com.wexad.daos.BaseDAO;
import com.wexad.domains.screen.Screen;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class ScreenDAO extends BaseDAO<Screen, UUID> {
    protected ScreenDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private final RowMapper<Screen> rowMapper = (rs, numRow) -> Screen.builder()
            .id(UUID.fromString(rs.getString("id")))
            .name(rs.getString("name"))
            .totalSeats(rs.getInt("totalSeats"))
            .isActive(rs.getBoolean("isActive"))
            .theaterId(UUID.fromString(rs.getString("theaterId")))
            .createdDate(rs.getTimestamp("createdDate").toLocalDateTime())
            .build();

    @Override
    public UUID save(Screen entity) {
        String sql = "select save_screen(?, ?, ?)";
        String id = jdbcTemplate.queryForObject(sql, String.class, entity.getTheaterId().toString(), entity.getName(), entity.getTotalSeats());
        return Objects.nonNull(id) ? UUID.fromString(id) : null;
    }

    @Override
    public void update(Screen entity) {
        String sql = "UPDATE screen SET name = ?, totalSeats = ? WHERE id = ?";
        jdbcTemplate.update(sql, entity.getName(), entity.getTotalSeats(), entity.getId().toString());
    }

    @Override
    public void delete(UUID uuid) {
        String sql = "UPDATE screen SET isActive = false WHERE id = ?";
        jdbcTemplate.update(sql, uuid.toString());
    }

    @Override
    public Screen findById(UUID uuid) {
        String sql = "SELECT * FROM screen WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, uuid.toString());
    }

    @Override
    public List<Screen> findAll() {
        return jdbcTemplate.query("SELECT * FROM screen", rowMapper);
    }

}
