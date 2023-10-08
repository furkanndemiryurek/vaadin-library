package core.service;

import java.util.List;

public interface GenericService <T>{
    void add(T entity);
    T findById(Long id);
    List<T> findAll();
    void deleteById(Long id);
    void update(T entity);
}
