package ua.dragunovskiy.mailing_service.security.dto;

import lombok.Data;

@Data
public class JwtRequestDto {
    private String username;
    private String password;
}
