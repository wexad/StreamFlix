package com.wexad.daos.show;

import com.wexad.daos.BaseDAO;
import com.wexad.domains.show.Show;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class ShowDAO extends BaseDAO<Show, UUID> {
    protected ShowDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private final RowMapper<Show> rowMapper = (rs, numRow) -> Show.builder()
            .id(UUID.fromString(rs.getString("id")))
            .movieId(UUID.fromString(rs.getString("movieId")))
            .screenId(UUID.fromString(rs.getString("screenId")))
            .isActive(rs.getBoolean("isActive"))
            .createdDate(rs.getTimestamp("createdDate").toLocalDateTime())
            .price(rs.getDouble("price"))
            .build();

    @Override
    public UUID save(Show entity) {
        String sql = "select save_show(?,?,?,?)";
        String id = jdbcTemplate.queryForObject(sql, String.class, entity.getMovieId(), entity.getScreenId(), entity.getShowTime(), entity.getPrice());
        return Objects.nonNull(id) ? UUID.fromString(id) : null;
    }

    @Override
    public void update(Show entity) {
        String sql = "UPDATE 'show' SET movieId=? AND screenId=? AND showTime=? AND price=? WHERE id=?;";
        jdbcTemplate.update(sql, entity.getMovieId(), entity.getScreenId(), entity.getShowTime(), entity.getPrice());
    }

    @Override
    public void delete(UUID uuid) {
        String sql = "UPDATE 'show' SET isActive = false WHERE id=?;";
        jdbcTemplate.update(sql, uuid);
    }

    @Override
    public Show findById(UUID uuid) {
        String sql = "SELECT * FROM show WHERE id=?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, uuid);
    }

    @Override
    public List<Show> findAll() {
        String sql = "SELECT * FROM 'show';";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
