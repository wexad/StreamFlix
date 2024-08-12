package com.wexad.service.bookedSeat;

import com.wexad.daos.bookedSeat.BookedSeatDAO;
import com.wexad.domains.bookedSeat.BookedSeat;
import com.wexad.service.BaseService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class BookedSeatService implements BaseService<BookedSeat, UUID> {


    private final BookedSeatDAO bookedSeatDAO;

    public BookedSeatService(BookedSeatDAO bookedSeatDAO) {
        this.bookedSeatDAO = bookedSeatDAO;
    }

    @Override
    public UUID save(BookedSeat domain) {
        return bookedSeatDAO.save(domain);
    }

    @Override
    public void update(BookedSeat domain) {
        bookedSeatDAO.update(domain);
    }

    @Override
    public void delete(UUID uuid) {
        bookedSeatDAO.delete(uuid);
    }

    @Override
    public BookedSeat findById(UUID uuid) {
        return bookedSeatDAO.findById(uuid);
    }

    @Override
    public List<BookedSeat> findAll() {
        return bookedSeatDAO.findAll();
    }

    public List<BookedSeat> findByShowId(UUID showId) {
        return bookedSeatDAO.findByShowId(showId);
    }


}
