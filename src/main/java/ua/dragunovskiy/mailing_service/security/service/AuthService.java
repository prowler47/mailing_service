package ua.dragunovskiy.mailing_service.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ua.dragunovskiy.mailing_service.security.dto.JwtRequestDto;
import ua.dragunovskiy.mailing_service.security.dto.JwtResponseDto;
import ua.dragunovskiy.mailing_service.security.dto.RegistrationUserDto;
import ua.dragunovskiy.mailing_service.security.dto.UserDto;
import ua.dragunovskiy.mailing_service.security.entity.UserEntity;
import ua.dragunovskiy.mailing_service.security.exception.AppError;
import ua.dragunovskiy.mailing_service.security.util.JwtTokenUtil;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(JwtRequestDto jwtRequestDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequestDto.getUsername(), jwtRequestDto.getPassword()));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Wrong login or password"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(jwtRequestDto.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponseDto(token));
    }


    public ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Passwords don't match"), HttpStatus.BAD_REQUEST);
        }
        if (userService.findUserByUsername(registrationUserDto.getUsername()).isPresent()) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "User with notice username is already exist"), HttpStatus.UNAUTHORIZED);
        }
        UserEntity user = userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername(), user.getPassword()));
    }

}
