package org.finance_manegement.server.repository;

import java.util.List;

public interface IRepository<T> {
    void create(T entity);
    List<T> readAll();
    T readById(int id);
    void update(T entity);
    void delete(T entity);
}
