package ua.dragunovskiy.mailing_service.security.controller;

import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.dragunovskiy.mailing_service.security.dto.JwtRequestDto;
import ua.dragunovskiy.mailing_service.security.dto.RegistrationUserDto;
import ua.dragunovskiy.mailing_service.security.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequestDto jwtRequestDto) {
       return authService.createAuthToken(jwtRequestDto);
    }




    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(registrationUserDto);
    }

}
