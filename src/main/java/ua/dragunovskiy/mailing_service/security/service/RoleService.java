package ua.dragunovskiy.mailing_service.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.dragunovskiy.mailing_service.security.entity.Role;
import ua.dragunovskiy.mailing_service.security.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }
}
