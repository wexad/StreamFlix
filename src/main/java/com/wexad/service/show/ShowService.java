package com.wexad.service.show;

import com.wexad.daos.show.ShowDAO;
import com.wexad.domains.show.Show;
import com.wexad.service.BaseService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
@Component
public class ShowService implements BaseService<Show, UUID> {

    private final ShowDAO showDAO;

    public ShowService(ShowDAO showDAO) {
        this.showDAO = showDAO;
    }

    @Override
    public UUID save(Show domain) {
        return showDAO.save(domain);
    }

    @Override
    public void update(Show domain) {
        showDAO.update(domain);
    }

    @Override
    public void delete(UUID uuid) {
        showDAO.delete(uuid);
    }

    @Override
    public Show findById(UUID uuid) {
        return showDAO.findById(uuid);
    }

    @Override
    public List<Show> findAll() {
        return showDAO.findAll();
    }

    public List<Show> findByScreenId(UUID screenId) {
        return showDAO.findByScreenId(screenId);
    }

    public List<Show> findByMovieId(UUID movieId) {
        return showDAO.findByMovieId(movieId);
    }
}
