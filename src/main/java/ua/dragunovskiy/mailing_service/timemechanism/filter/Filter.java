package ua.dragunovskiy.mailing_service.timemechanism.filter;

import ua.dragunovskiy.mailing_service.entity.Notification;

import java.util.List;

public interface Filter<T> {
    List<T> filter();
}
