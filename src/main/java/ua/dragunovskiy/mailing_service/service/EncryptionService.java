package ua.dragunovskiy.mailing_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EncryptionService {

    public String encodeUsername(String rawUsername) {
        return Base64.getEncoder().encodeToString(rawUsername.getBytes());
    }

    public String decodeUsername(String encodedUsername) {
        byte[] decode = Base64.getDecoder().decode(encodedUsername);
        return new String(decode);
    }
}
