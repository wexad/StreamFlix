package com.wexad.daos.booking;

import com.wexad.daos.BaseDAO;
import com.wexad.domains.booking.Booking;
import com.wexad.dto.DataDTO;
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
    private final RowMapper<DataDTO> dataDTORowMapper = (rs, numRow) -> DataDTO.builder()
            .theaterName(rs.getString("theaterName"))
            .theaterLocation(rs.getString("theaterLocation"))
            .screenName(rs.getString("screenName"))
            .showTime(rs.getTimestamp("showTime").toLocalDateTime())
            .showPrice(rs.getDouble("showPrice"))
            .seatNumbers(rs.getString("seatNumbers"))
            .build();

    @Override
    public UUID save(Booking entity) {
        String sql = "Select save_booking(?, ?, ? , ?)";
        String id = jdbcTemplate.queryForObject(sql, String.class, entity.getUserId().toString(), entity.getShowId().toString(), entity.getSeatNumber(), entity.getCardNumber());
        return Objects.nonNull(id) ? UUID.fromString(id) : null;
    }

    @Override
    public void update(Booking entity) {
        String sql = "UPDATE booking SET seatNumber = ?, cardNumber ?, isActive = ? WHERE id = ?";
        jdbcTemplate.update(sql, entity.getSeatNumber(), entity.getCardNumber(), entity.isActive(), entity.getId().toString());
    }

    @Override
    public void delete(UUID uuid) {
        String sql = "UPDATE booking SET isActive = false WHERE id = ?";
        jdbcTemplate.update(sql, uuid.toString());
    }

    @Override
    public Booking findById(UUID uuid) {
        String sql = "SELECT * FROM booking WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, uuid.toString());
    }

    @Override
    public List<Booking> findAll() {
        return jdbcTemplate.query("SELECT * FROM booking", rowMapper);
    }

    public List<Booking> findByUserId(UUID userId) {
        String sql = "select * from booking where userId = ?";
        return jdbcTemplate.query(sql, rowMapper, userId.toString());
    }

    public Integer getCountOfTicketByUser(UUID userId, UUID showId) {
        String sql = "select count(*) from booking where userId = ? and showId = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId.toString(), showId.toString());
    }

    public List<DataDTO> getData(UUID userId) {
        String sql = """
                select theater.name as theaterName,
                       theater.location as theaterLocation,
                       screen.name as screenName,
                       `show`.showTime as showTime,
                       `show`.price as showPrice,
                       group_concat(booking.seatNumber separator ', ') as seatNumbers
                from users
                inner join booking on users.id = booking.userId
                inner join `show` on booking.showId = `show`.id
                inner join movie on `show`.movieId = movie.id
                inner join screen on `show`.screenId = screen.id
                inner join theater on screen.theaterId = theater.id
                where users.id = ?
                group by theater.name, theater.location, screen.name, `show`.showTime, `show`.price;
                """;
        return jdbcTemplate.query(sql, dataDTORowMapper, userId.toString());
    }

    public Boolean isUserSawMovie(UUID userId, UUID movieId) {
        String sql = """
                 select exists(
                 select 1 from booking
                 inner join users on booking.userId = users.id
                 inner join `show` on booking.showId = `show`.id
                 inner join movie on `show`.movieId = movie.id
                 where userId = ? and
                       movieId = ? and
                       TIMESTAMPADD(minute, movie.duration, `show`.showTime) > current_timestamp)
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, userId, movieId);
    }


}
