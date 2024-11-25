package ua.dragunovskiy.mailing_service.security.repository;

import org.springframework.data.repository.CrudRepository;
import ua.dragunovskiy.mailing_service.security.entity.Role;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
