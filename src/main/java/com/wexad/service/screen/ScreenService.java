package com.wexad.service.screen;

import com.wexad.daos.screen.ScreenDAO;
import com.wexad.domains.screen.Screen;
import com.wexad.service.BaseService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
@Component
public class ScreenService implements BaseService<Screen, UUID> {
    private final ScreenDAO screenDAO;

    public ScreenService(ScreenDAO screenDAO) {
        this.screenDAO = screenDAO;
    }

    @Override
    public UUID save(Screen domain) {
        return screenDAO.save(domain);
    }

    @Override
    public void update(Screen domain) {
        screenDAO.update(domain);
    }

    @Override
    public void delete(UUID uuid) {
        screenDAO.delete(uuid);
    }

    @Override
    public Screen findById(UUID uuid) {
        return screenDAO.findById(uuid);
    }

    @Override
    public List<Screen> findAll() {
        return screenDAO.findAll();
    }
}
