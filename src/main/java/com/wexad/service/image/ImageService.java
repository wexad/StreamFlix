package com.wexad.service.image;

import com.wexad.daos.image.ImageDAO;
import com.wexad.domains.image.Image;
import com.wexad.service.BaseService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
@Component
public class ImageService implements BaseService<Image, UUID> {

    private final ImageDAO imageDAO;

    public ImageService(ImageDAO imageDAO) {
        this.imageDAO = imageDAO;
    }

    @Override
    public UUID save(Image domain) {
        return imageDAO.save(domain);
    }

    @Override
    public void update(Image domain) {
        imageDAO.update(domain);
    }

    @Override
    public void delete(UUID uuid) {
        imageDAO.delete(uuid);
    }

    @Override
    public Image findById(UUID uuid) {
        return imageDAO.findById(uuid);
    }

    @Override
    public List<Image> findAll() {
        return imageDAO.findAll();
    }
}
