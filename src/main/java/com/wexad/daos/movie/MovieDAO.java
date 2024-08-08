package com.wexad.daos.movie;

import com.wexad.daos.BaseDAO;
import com.wexad.domains.movie.Movie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class MovieDAO extends BaseDAO<Movie, UUID> {
    protected MovieDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private final RowMapper<Movie> rowMapper = (rs, numRow) -> Movie.builder()
            .id(UUID.fromString(rs.getString("id")))
            .name(rs.getString("name"))
            .title(rs.getString("title"))
            .isActive(rs.getBoolean("isActive"))
            .createdDate(rs.getTimestamp("createdDate").toLocalDateTime())
            .genre(rs.getString("genre"))
            .trailerUrl(rs.getString("trailerUrl"))
            .duration(rs.getInt("duration"))
            .rating(rs.getDouble("rating"))
            .build();

    @Override
    public UUID save(Movie entity) {
        String sql = "select save_movie(?, ?, ?, ?, ?, ?)";
        String id = jdbcTemplate.queryForObject(sql, String.class, entity.getCreatedDate(), entity.getName(), entity.getTitle(), entity.getTrailerUrl(), entity.getGenre(), entity.getDuration());
        return Objects.nonNull(id) ? UUID.fromString(id) : null;
    }

    @Override
    public void update(Movie entity) {
        String sql = "UPDATE movie SET name = ?, title = ?, genre = ?, trailerUrl = ?, duration = ?, rating = ? WHERE id = ?";
        jdbcTemplate.update(sql, entity.getName(), entity.getTitle(), entity.getGenre(), entity.getTrailerUrl(), entity.getDuration(), entity.getRating(), entity.getId());
    }

    @Override
    public void delete(UUID uuid) {
        String sql = "UPDATE movie SET isActive = false WHERE id = ?";
        jdbcTemplate.update(sql, uuid);
    }

    @Override
    public Movie findById(UUID uuid) {
        String sql = "SELECT * FROM movie WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, uuid);
    }

    @Override
    public List<Movie> findAll() {
        return jdbcTemplate.query("SELECT * FROM movie", rowMapper);
    }
}
