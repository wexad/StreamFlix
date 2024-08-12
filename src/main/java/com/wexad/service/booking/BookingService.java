package com.wexad.service.booking;

import com.wexad.daos.booking.BookingDAO;
import com.wexad.domains.booking.Booking;
import com.wexad.dto.DataDTO;
import com.wexad.service.BaseService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class BookingService implements BaseService<Booking, UUID> {
    private final BookingDAO bookingDAO;

    public BookingService(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    @Override
    public UUID save(Booking domain) {
        return bookingDAO.save(domain);
    }

    @Override
    public void update(Booking domain) {
        bookingDAO.update(domain);
    }

    @Override
    public void delete(UUID uuid) {
        bookingDAO.delete(uuid);
    }

    @Override
    public Booking findById(UUID uuid) {
        return bookingDAO.findById(uuid);
    }

    @Override
    public List<Booking> findAll() {
        return bookingDAO.findAll();
    }

    public List<Booking> findByUserId(UUID userId) {
        return bookingDAO.findByUserId(userId);
    }

    public Integer getCountOfTicketByUser(UUID userId, UUID showId) {
        return bookingDAO.getCountOfTicketByUser(userId, showId);
    }
    public List<DataDTO> getData(UUID userId) {
        return bookingDAO.getData(userId);
    }

    public Boolean isUserSawMovie(UUID userId, UUID movieId) {
        return bookingDAO.isUserSawMovie(userId, movieId);
    }
}
