package com.wexad.service.theater;

import com.wexad.daos.theater.TheaterDAO;
import com.wexad.domains.theater.Theater;
import com.wexad.service.BaseService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
@Component
public class TheaterService implements BaseService<Theater, UUID> {

    private final TheaterDAO theaterDAO;

    public TheaterService(TheaterDAO theaterDAO) {
        this.theaterDAO = theaterDAO;
    }

    @Override
    public UUID save(Theater domain) {
        return theaterDAO.save(domain);
    }

    @Override
    public void update(Theater domain) {
        theaterDAO.update(domain);
    }

    @Override
    public void delete(UUID uuid) {
        theaterDAO.delete(uuid);
    }

    @Override
    public Theater findById(UUID uuid) {
        return theaterDAO.findById(uuid);
    }

    @Override
    public List<Theater> findAll() {
        return theaterDAO.findAll();
    }
}
