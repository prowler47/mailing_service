package ua.dragunovskiy.mailing_service.timemechanism.filter;

import java.util.List;

public interface Filter<T> {
    List<T> filterForSending();

    void filterForDelete();


}
