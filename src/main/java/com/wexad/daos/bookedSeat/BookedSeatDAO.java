package com.wexad.daos.bookedSeat;

import com.wexad.daos.BaseDAO;
import com.wexad.domains.bookedSeat.BookedSeat;
import com.wexad.domains.booking.Booking;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class BookedSeatDAO extends BaseDAO<BookedSeat, UUID> {
    protected BookedSeatDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private final RowMapper<BookedSeat> rowMapper = (rs, numRow) -> BookedSeat.builder()
            .id(UUID.fromString(rs.getString("id")))
            .showId(rs.getString("showId"))
            .isActive(rs.getBoolean("isActive"))
            .seatNumber(rs.getInt("seatNumber"))
            .createdDate(rs.getTimestamp("createdDate").toLocalDateTime())
            .build();

    @Override
    public UUID save(BookedSeat entity) {
        String sql = "select save_bookedSeat(?, ?)";
        return jdbcTemplate.queryForObject(sql, UUID.class, entity.getShowId(), entity.getSeatNumber());
    }

    @Override
    public void update(BookedSeat entity) {
        String sql = "update bookedSeat set showId = ?, seatNumber = ? where showId = ?";
        jdbcTemplate.update(sql, entity.getShowId(), entity.getSeatNumber(), entity.getShowId().toString());
    }

    @Override
    public void delete(UUID uuid) {
        String sql = "update bookedSeat set isActive = false where showId = ?";
        jdbcTemplate.update(sql, uuid.toString());
    }

    @Override
    public BookedSeat findById(UUID uuid) {
        String sql = "select * from bookedSeat where showId = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, uuid.toString());
    }

    @Override
    public List<BookedSeat> findAll() {
        String sql = "select * from bookedSeat";
        return jdbcTemplate.query(sql, rowMapper);
    }
    public List<BookedSeat> findByShowId(UUID showId) {
        String sql = "select * from bookedSeat where showId = ?";
        return jdbcTemplate.query(sql, rowMapper, showId.toString());
    }


}
