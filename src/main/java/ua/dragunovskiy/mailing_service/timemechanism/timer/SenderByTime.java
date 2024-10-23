package ua.dragunovskiy.mailing_service.timemechanism.timer;

import lombok.Data;
import org.springframework.stereotype.Component;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.sender.Sender;
import ua.dragunovskiy.mailing_service.sender.SenderType;
import ua.dragunovskiy.mailing_service.sender.SimpleMailSender;
import ua.dragunovskiy.mailing_service.sender.ChooseSender;
import ua.dragunovskiy.mailing_service.util.Time;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Data
public class SenderByTime {

    private final SimpleMailSender simpleMailSender;

    Set<Notification> sentNotificationsMail = new HashSet<>();
    Set<Notification> sentNotificationsTelegram = new HashSet<>();
    Set<Notification> sentNotificationsViber = new HashSet<>();
    Set<UUID> sentNotificationsUUIDList = new HashSet<>();


    public void sendNotificationByTime(List<Notification> notificationsForSend, int interval, List<SenderType> senderTypes) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Notification notification : notificationsForSend) {
                    for (SenderType senderType : senderTypes) {
                        Sender sender = new ChooseSender(simpleMailSender).chooseSender(senderType);
                        if (timeChecker(notification.getDate())) {
                            if (sender.getType().equals("mailSender")) {
                                if (!sentNotificationsMail.contains(notification)) {
                                    if (!sentNotificationsUUIDList.contains(notification.getId())) {
                                        sendNotificationAndDistributeNotificationForSentLists(notification, sender);
                                        sentNotificationsUUIDList.add(notification.getId());
                                    }
                                }
                            }
                            if (sender.getType().equals("telegramSender")) {
                                if (!sentNotificationsTelegram.contains(notification)) {
                                    sendNotificationAndDistributeNotificationForSentLists(notification, sender);
                                }
                            }
                            if (sender.getType().equals("viberSender")) {
                                if (!sentNotificationsViber.contains(notification)) {
                                    sendNotificationAndDistributeNotificationForSentLists(notification, sender);
                                }
                            }
                        }
                    }
                }
            }
        }, 0, interval);
    }

    private void sendNotificationAndDistributeNotificationForSentLists(Notification notification, Sender sender) {
            sender.send(notification.getAddress(), notification.getTitle(), notification.getPayload() + ", date: " + notification.getDate());
            notificationsSenderDistribute(notification, sender, sentNotificationsMail, sentNotificationsTelegram, sentNotificationsViber);
//        sender.send(notification.getAddress(), notification.getTitle(), notification.getPayload() + ", date: " + notification.getDate());
//        notificationsSenderDistribute(notification, sender, sentNotificationsMail, sentNotificationsTelegram, sentNotificationsViber);
    }

    private boolean timeChecker(String time) {
        String notificationTime = time.split(" ")[1];
        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentDateTime = simpleDateFormat.format(currentDate).split(" ")[1];
        return notificationTime.equals(currentDateTime);
    }

    private void notificationsSenderDistribute(Notification notification, Sender sender, Set<Notification> mail, Set<Notification> telegram, Set<Notification> viber) {
        if (sender.getType().equals("mailSender")) {
            mail.add(notification);
        } else if (sender.getType().equals("telegramSender")) {
            telegram.add(notification);
        } else if (sender.getType().equals("viberSender")) {
            viber.add(notification);
        }
    }

    public void clearSentLists() {
        System.out.println("Sent lists clear started..." + Time.getCurrentTime());
        Set<Notification> mailSet = filterToClear(sentNotificationsMail);
        Set<Notification> telegramSet = filterToClear(sentNotificationsTelegram);
        Set<Notification> viberSet = filterToClear(sentNotificationsViber);
        sentNotificationsMail.removeIf(mailSet::contains);
        sentNotificationsTelegram.removeIf(telegramSet::contains);
        sentNotificationsViber.removeIf(viberSet::contains);

        for (Notification notification : sentNotificationsMail) {
            System.out.println(notification.getPayload() + " " + notification.getTitle() + " - " + notification.getDate());
        }
        for (Notification notification : sentNotificationsTelegram) {
            System.out.println(notification.getPayload() + " " + notification.getTitle() + " - " + notification.getDate());
        }
        for (Notification notification : sentNotificationsViber) {
            System.out.println(notification.getPayload() + " " + notification.getTitle() + " - " + notification.getDate());
        }
        System.out.println("Sent lists clear finished..." + Time.getCurrentTime());
    }

    private String separateTimeFromDate(String date) {
        return date.split(" ")[1];
    }

    private Set<Notification> filterToClear(Set<Notification> notificationList) {
        Set<Notification> notificationsForClear = new HashSet<>();
        for (Notification notification : notificationList) {
            if (Time.timeComparator(separateTimeFromDate(Time.getCurrentTime()), separateTimeFromDate(notification.getDate()))) {
                notificationsForClear.add(notification);
            }
        }
        return notificationsForClear;
    }
}
