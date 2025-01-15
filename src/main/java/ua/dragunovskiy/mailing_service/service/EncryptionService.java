package ua.dragunovskiy.mailing_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.dragunovskiy.mailing_service.exception.UsernameFromCookieNotFound;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EncryptionService {

    public String encodeUsername(String rawUsername) {
        if (rawUsername != null) {
            return Base64.getEncoder().encodeToString(rawUsername.getBytes());
        }
        throw new UsernameFromCookieNotFound("Username from cookie not found");
    }

    public String decodeUsername(String encodedUsername) {
        byte[] decode = Base64.getDecoder().decode(encodedUsername);
        return new String(decode);
    }
}
