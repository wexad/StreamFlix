package com.wexad.daos.image;

import com.wexad.daos.BaseDAO;
import com.wexad.domains.image.Image;
import com.wexad.domains.theater.Theater;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class ImageDAO extends BaseDAO<Image, UUID> {
    protected ImageDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private final RowMapper<Image> rowMapper = (rs, numRow) -> Image.builder()
            .id(UUID.fromString(rs.getString("id")))
            .mimeType(rs.getString("mimeType"))
            .extension(rs.getString("extension"))
            .isActive(rs.getBoolean("isActive"))
            .movieId(UUID.fromString(rs.getString("movieId")))
            .createdDate(rs.getTimestamp("createdDate").toLocalDateTime())
            .build();

    @Override
    public UUID save(Image entity) {
        String sql = "select save_image(?, ?, ?)";
        String id = jdbcTemplate.queryForObject(sql, String.class, entity.getMimeType(), entity.getExtension(), entity.getMovieId());
        return Objects.nonNull(id) ? UUID.fromString(id) : null;
    }

    @Override
    public void update(Image entity) {
        String sql = "UPDATE image SET mimeType = ?, extension = ?, isActive = ? WHERE id = ?";
        jdbcTemplate.update(sql, entity.getMimeType(), entity.getExtension(), entity.isActive(), entity.getId());
    }

    @Override
    public void delete(UUID uuid) {
        String sql = "UPDATE image SET isActive = false WHERE id = ?";
        jdbcTemplate.update(sql, uuid);
    }

    @Override
    public Image findById(UUID uuid) {
        String sql = "SELECT * FROM image WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, uuid);
    }

    @Override
    public List<Image> findAll() {
        return jdbcTemplate.query("SELECT * FROM image", rowMapper);
    }
}
