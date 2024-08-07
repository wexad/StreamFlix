package com.wexad.daos;

import com.wexad.domains.BaseDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;


@Component
public abstract class  BaseDAO<T extends BaseDomain, ID extends Serializable> {

    protected final JdbcTemplate jdbcTemplate;

    @Autowired
    protected BaseDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public abstract ID save(T entity);

    public abstract void update(T entity);

    public abstract void delete(ID id);

    public abstract T findById(ID id);

    public abstract List<T> findAll();
}

