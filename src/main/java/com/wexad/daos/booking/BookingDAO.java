package com.wexad.daos.booking;

import com.wexad.daos.BaseDAO;
import com.wexad.domains.booking.Booking;
import com.wexad.domains.theater.Theater;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Component
public class BookingDAO extends BaseDAO<Booking, UUID> {
    protected BookingDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }
    private final RowMapper<Booking> rowMapper = (rs, numRow) -> Booking.builder()
            .id(UUID.fromString(rs.getString("id")))
            .userId(UUID.fromString(rs.getString("userId")))
            .showId(UUID.fromString(rs.getString("showId")))
            .isActive(rs.getBoolean("isActive"))
            .seatNumber(rs.getInt("seatNumber"))
            .cardNumber(rs.getLong("cardNumber"))
            .createdDate(rs.getTimestamp("createdDate").toLocalDateTime())
            .build();
    @Override
    public UUID save(Booking entity) {
        String sql = "Select save_booking(?, ?, ? , ?)";
        String id = jdbcTemplate.queryForObject(sql, String.class, entity.getUserId(), entity.getShowId(), entity.getSeatNumber(), entity.getCardNumber());
        return Objects.nonNull(id) ? UUID.fromString(id) : null;
    }

    @Override
    public void update(Booking entity) {
        String sql = "UPDATE booking SET seatNumber = ?, cardNumber ?, isActive = ? WHERE id = ?";
        jdbcTemplate.update(sql, entity.getSeatNumber(), entity.getCardNumber(), entity.isActive(), entity.getId());
    }

    @Override
    public void delete(UUID uuid) {
        String sql = "UPDATE booking SET isActive = false WHERE id = ?";
        jdbcTemplate.update(sql, uuid);
    }

    @Override
    public Booking findById(UUID uuid) {
        String sql = "SELECT * FROM booking WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, uuid);
    }

    @Override
    public List<Booking> findAll() {
        return jdbcTemplate.query("SELECT * FROM booking", rowMapper);
    }
}
