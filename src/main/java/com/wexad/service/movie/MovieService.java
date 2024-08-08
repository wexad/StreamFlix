package com.wexad.service.movie;

import com.wexad.daos.movie.MovieDAO;
import com.wexad.domains.movie.Movie;
import com.wexad.service.BaseService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class MovieService implements BaseService<Movie, UUID> {
    private final MovieDAO movieDAO;

    public MovieService(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    @Override
    public UUID save(Movie domain) {
        return movieDAO.save(domain);
    }

    @Override
    public void update(Movie domain) {
        movieDAO.update(domain);
    }

    @Override
    public void delete(UUID uuid) {
        movieDAO.delete(uuid);
    }

    @Override
    public Movie findById(UUID uuid) {
        return movieDAO.findById(uuid);
    }

    @Override
    public List<Movie> findAll() {
        return movieDAO.findAll();
    }
}
