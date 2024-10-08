package ua.dragunovskiy.mailing_service.dao;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Dao<ID, E> {
    void add(E entity);
    List<E> getAll();
    E getById(ID id);
    void delete(ID id);
}
