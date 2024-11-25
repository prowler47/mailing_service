package ua.dragunovskiy.mailing_service.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.dragunovskiy.mailing_service.security.dto.RegistrationUserDto;
import ua.dragunovskiy.mailing_service.security.entity.UserEntity;
import ua.dragunovskiy.mailing_service.security.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    public Optional<UserEntity> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User '$s' not found", username)));
        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
    }

    public UserEntity createNewUser(RegistrationUserDto registrationUserDto) {
        UserEntity user = new UserEntity();
        user.setUsername(registrationUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setEmail(registrationUserDto.getEmail());
        user.setRoles(List.of(roleService.getUserRole()));
        return userRepository.save(user);
    }
}
