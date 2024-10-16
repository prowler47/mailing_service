package ua.dragunovskiy.mailing_service.timemechanism.timer;

import lombok.Data;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Component;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.sender.Sender;
import ua.dragunovskiy.mailing_service.sender.SenderType;
import ua.dragunovskiy.mailing_service.sender.senderutil.ChooseSender;
import ua.dragunovskiy.mailing_service.util.Time;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Data
public class SenderByTime {

    List<Notification> sentNotificationsMail = new ArrayList<>();
    List<Notification> sentNotificationsTelegram = new ArrayList<>();
    List<Notification> sentNotificationsViber = new ArrayList<>();

    public void sendNotificationByTime(List<Notification> notificationsForSend, int interval, List<SenderType> senderTypes) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Notification notification : notificationsForSend) {
                    for (SenderType senderType : senderTypes) {
                        Sender sender = ChooseSender.chooseSender(senderType);
                        if (timeChecker(notification.getDate())) {
                            if (sender.getType().equals("mailSender")) {
                                if (!sentNotificationsMail.contains(notification)) {
                                    sendNotificationAndDistributeNotificationForSentLists(notification, sender);
                                }
                                System.out.println(sentNotificationsMail);
                            }
                            if (sender.getType().equals("telegramSender")) {
                                if (!sentNotificationsTelegram.contains(notification)) {
                                    sendNotificationAndDistributeNotificationForSentLists(notification, sender);
                                }
                                System.out.println(sentNotificationsTelegram);
                            }
                            if (sender.getType().equals("viberSender")) {
                                if (!sentNotificationsViber.contains(notification)) {
                                    sendNotificationAndDistributeNotificationForSentLists(notification, sender);
                                }
                                System.out.println(sentNotificationsViber);
                            }
                        }
                    }
                }
            }
        }, 0, interval);
    }

    private void sendNotificationAndDistributeNotificationForSentLists(Notification notification, Sender sender) {
        sender.send(notification.getAddress(), notification.getTitle(), notification.getPayload() + ", date: " + notification.getDate());
        notificationsSenderDistributor(notification, sender, sentNotificationsMail, sentNotificationsTelegram, sentNotificationsViber);
    }

    private boolean timeChecker(String time) {
        String notificationTime = time.split(" ")[1];
        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentDateTime = simpleDateFormat.format(currentDate).split(" ")[1];
        return notificationTime.equals(currentDateTime);
    }

    private void notificationsSenderDistributor(Notification notification, Sender sender, List<Notification> mail, List<Notification> telegram, List<Notification> viber) {
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
            System.out.println(notification.getPayload() + " " + notification.getTitle());
        }
        for (Notification notification : sentNotificationsTelegram) {
            System.out.println(notification.getPayload() + " " + notification.getTitle());
        }
        for (Notification notification : sentNotificationsViber) {
            System.out.println(notification.getPayload() + " " + notification.getTitle());
        }
        System.out.println("Sent lists clear finished..." + Time.getCurrentTime());
    }

    private String separateMinutesForDate(String date) {
        String time = date.split(" ")[1];
        return time.split(":")[1];
    }

    private int getIntegerFromCurrentTimeInMinutes() {
        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentDateTime = simpleDateFormat.format(currentDate).split(" ")[1];
        return Integer.parseInt(currentDateTime.split(":")[1]);
    }



    private Set<Notification> filterToClear(List<Notification> notificationList) {
        Set<Notification> notificationsForClear = new HashSet<>();
        for (Notification notification : notificationList) {
            String stringMinutes = separateMinutesForDate(notification.getDate());
            int notificationSendMinutes = Integer.parseInt(stringMinutes);
            int currentMinutes = getIntegerFromCurrentTimeInMinutes();
            if (currentMinutes > notificationSendMinutes + 2) {
                notificationsForClear.add(notification);
            }
        }
        return notificationsForClear;
    }
}
