package ua.dragunovskiy.mailing_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EncryptionService {

    public String encodeUsername(String rawUsername) {
        if (rawUsername != null) {
            return Base64.getEncoder().encodeToString(rawUsername.getBytes());
        }
        return null;
    }

    public String decodeUsername(String encodedUsername) {
        byte[] decode = Base64.getDecoder().decode(encodedUsername);
        return new String(decode);
    }
}
