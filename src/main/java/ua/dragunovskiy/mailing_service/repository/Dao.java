package ua.dragunovskiy.mailing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Dao<ID, E> {

    E save(E entity);

    void update(ID updatedEntityId, E entityForUpdate);
    List<E> getAll();
    E getById(ID id);
    void delete(ID id);
}
