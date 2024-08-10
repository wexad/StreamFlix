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
            .originalName(rs.getString("originalName"))
            .isActive(rs.getBoolean("isActive"))
            .movieId(UUID.fromString(rs.getString("movieId")))
            .extension(rs.getString("extension"))
            .createdDate(rs.getTimestamp("createdDate").toLocalDateTime())
            .build();

    @Override
    public UUID save(Image entity) {
        String sql = "select save_image(?, ?, ?, ?, ?)";
        String id = jdbcTemplate.queryForObject(sql, String.class, entity.getId().toString(), entity.getMimeType(), entity.getOriginalName(), entity.getMovieId().toString(), entity.getExtension());
        return Objects.nonNull(id) ? UUID.fromString(id) : null;
    }

    @Override
    public void update(Image entity) {
        String sql = "UPDATE image SET mimeType = ?, generatedName = ?, isActive = ? WHERE id = ?";
        jdbcTemplate.update(sql, entity.getMimeType(), entity.getOriginalName(), entity.isActive(), entity.getId());
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
