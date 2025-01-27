package ua.dragunovskiy.mailing_service.security.storage;


import java.util.List;
import java.util.Set;

public interface UserNameStorage<T> {
    List<T> getStorage();
    void putUsernameToStorage(T username);
    T getUsernameFromStorage();
}
