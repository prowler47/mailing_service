package ua.dragunovskiy.mailing_service.security.dto;

import lombok.Data;

@Data
public class JwtResponseDto {
    public JwtResponseDto(String token) {
        this.token = token;
    }

    private String token;
}
