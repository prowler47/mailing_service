package ua.dragunovskiy.mailing_service.security.storage;

import org.springframework.stereotype.Component;
import ua.dragunovskiy.mailing_service.exception.UsernameFromCookieNotFound;

import java.util.*;

@Component
public class SimpleUserNameStorage implements UserNameStorage<String> {
    public static List<String> storage = new ArrayList<>();

    @Override
    public List<String> getStorage() {
        return storage;
    }

    @Override
    public void putUsernameToStorage(String username) {
        storage.clear();
        storage.add(username);
    }

//    @Override
//    public String getUsernameFromStorage() {
//        if (!storage.isEmpty()) {
//            return storage.get(0);
//        }
//        return "Storage is empty";
//    }

    @Override
    public String getUsernameFromStorage() {
        if (!storage.isEmpty()) {
            return storage.get(0);
        }
        System.out.println("Storage of user names is empty");
        return null;
    }
}


