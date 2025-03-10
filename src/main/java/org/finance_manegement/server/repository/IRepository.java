package org.finance_manegement.server.repository;

import java.util.List;

public interface IRepository<T> {
    void create(T entity);
    List<T> findAll();
    T findById(int id);
    void update(T entity, int id);
    void delete(int id);
}
