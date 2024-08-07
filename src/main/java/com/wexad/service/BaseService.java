package com.wexad.service;

import com.wexad.domains.BaseDomain;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
public interface BaseService<T extends BaseDomain, ID extends Serializable> {
    ID save(T domain);

    void update(T domain);

    void delete(ID id);

    T findById(ID id);

    List<T> findAll();
}
