package com.wexad.service.review;

import com.wexad.daos.review.ReviewDAO;
import com.wexad.domains.review.Review;
import com.wexad.service.BaseService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
@Component
public class ReviewService implements BaseService<Review, UUID> {
    private final ReviewDAO reviewDAO;

    public ReviewService(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    @Override
    public UUID save(Review domain) {
        return reviewDAO.save(domain);
    }

    @Override
    public void update(Review domain) {
        reviewDAO.update(domain);
    }

    @Override
    public void delete(UUID uuid) {
        reviewDAO.delete(uuid);
    }

    @Override
    public Review findById(UUID uuid) {
        return reviewDAO.findById(uuid);
    }

    @Override
    public List<Review> findAll() {
        return reviewDAO.findAll();
    }
}
