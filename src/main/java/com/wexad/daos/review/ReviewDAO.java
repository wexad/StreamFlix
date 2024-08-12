package com.wexad.daos.review;

import com.wexad.daos.BaseDAO;
import com.wexad.domains.review.Review;
import com.wexad.domains.theater.Theater;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class ReviewDAO extends BaseDAO<Review, UUID> {
    protected ReviewDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private final RowMapper<Review> rowMapper = (rs, numRow) -> Review.builder()
            .id(UUID.fromString(rs.getString("id")))
            .movieId(UUID.fromString(rs.getString("movieId")))
            .userId(UUID.fromString(rs.getString("userId")))
            .isActive(rs.getBoolean("isActive"))
            .rating(rs.getDouble("rating"))
            .comment(rs.getString("comment"))
            .createdDate(rs.getTimestamp("createdDate").toLocalDateTime())
            .build();

    @Override
    public UUID save(Review entity) {
        String sql = "select save_review(?, ?, ?, ?)";
        String id = jdbcTemplate.queryForObject(sql, String.class, entity.getMovieId().toString(), entity.getUserId().toString(), entity.getRating(), entity.getComment());
        return Objects.nonNull(id) ? UUID.fromString(id) : null;
    }

    @Override
    public void update(Review entity) {
        String sql = "UPDATE review SET rating=?, comment=? WHERE id=?";
        jdbcTemplate.update(sql, entity.getRating(), entity.getComment(), entity.getId().toString());
    }

    @Override
    public void delete(UUID uuid) {
        String sql = "UPDATE review SET isActive = false WHERE id=?";
        jdbcTemplate.update(sql, uuid.toString());
    }

    @Override
    public Review findById(UUID uuid) {
        String sql = "SELECT * FROM review WHERE id=?";
        return jdbcTemplate.queryForObject(sql, rowMapper, uuid.toString());
    }

    @Override
    public List<Review> findAll() {
        return jdbcTemplate.query("SELECT * FROM review", rowMapper);
    }

    public List<Review> findByMovieId(UUID movieId) {
        String sql = "SELECT * FROM review WHERE movieId=?";
        return jdbcTemplate.query(sql, rowMapper, movieId.toString());
    }
}
