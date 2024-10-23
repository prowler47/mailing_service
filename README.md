# Documentation for Mailing service:
***
## Structure of project:
* **controller**
    * NotificationController
    * TaskByTimerController
    * TestController
* **dao**
    * Dao
    * NotificationDao
    * TestPayLoadDao
* **entity**
    * Notification
    * TimerTestEntity
* **sender**
   * senderutil
      * ChooseSender
   * Sender
   * MailSender
   * TelegramSender
   * ViberSender
   * SenderType
* **service**
    * NotificationService
* **timemechanism**
    * **check**
      * CheckNotifications
      * SimpleCheckNotifications
      * TestCheckNotifications
    * **comparator**
      * DateComparator
    * **filter**
      * Filter
      * SimpleFilterNotifications
    * **timer**
      * TaskByTimer
      * SenderByTime
* **util**
    * FromStringToDateParser
    * NotificationToConsolePrinter
    * Time
* MailingServiceNotification

***
## More details description:
* **controller**
    * NotificationController:
  Simple controller contains endpoints for work with notifications:
      * *addNotification*
      * *findAndDeleteOverdueNotifications*
      * *deleteNotification*
    * TaskByTimerController: controller for work with methods which depends on
  timer
      * *startTaskByTimer* - start task with some repeat interval
    * TestController - just controller for some tests endpoints
* **dao**
    * Dao: common interface with CRUD methods for another implementations for 
  interaction with DB
    * NotificationDao - dao for notifications
    * TestPayLoadDao - dao for payload (for testing)
* **entity**
    * Notification
    * TimerTestEntity

* **sender**
    * **senderutil**
        * ChooseSender - utility class for specify one of the senders list
          * *chooseSender* - method for set some sender. It takes SenderType
      and return one of Sender implementations
    * Sender - common interface for all senders
        * *send* - method for send some message to client
    * MailSender - implementation of Sender for work with email
      * *send* - send message to email
    * TelegramSender - implementation of Sender for work with Telegram
        * *send* - send message to Telegram
    * ViberSender - implementation of Sender for work with Viber
        * *send* - send message to Viber
    * SenderType - enum with all kinds of senders names
* ***service***
    * NotificationService - service layer for working with notifications
* ***timemechanism***
    * ***check***
        * CheckNotifications - interface for checking notifications. it
      can send or delete notification
          * *checkNotificationsForSend*
          * *checkNotificationsForSendWithSenderTypes*
          * *checkNotificationsForDelete*
        * SimpleCheckNotifications - it's implementation of CheckNotification.
      It simplest version of CheckNotifications
          * *checkNotificationsForSend* - find only notification which must be
          send in present period. It takes Filter as argument. It will
          send some message to email by default.
          * *checkNotificationsForSendWithSenderTypes* - find only notification which must be
            send in present period. It takes Filter as argument. It also takes one or
          more senders as argument
          * *checkNotificationsForDelete* - find only notification which must be
            deleted
        * TestCheckNotifications - it's implementation of CheckNotification only
      for testing
    * ***comparator***
      * DateComparator - class for comparing date from notification and
      present date
          * *compareActualDate* - compare date of notification and current date in some period in
        minutes. If date from notification present in current date and some period it return true. In 
        another cases it return false
         * *compareOverdueDate* - compare date from notification and actual date. It return true
        It return true is notification date is overdue and it need to delete from DB
    * ***filter***
        * Filter - common interface for filters. It filter only notifications
  for send and notifications for delete
          * *filterForSending* - method for filter only notifications
      for sending
          * *filterForDelete* - method for filter only notifications
      for delete
      * SimpleFilterNotifications - the simplest implementation of Filter.
  It override methods from Filter as default behavior
    * ***timer***
      * TaskByTimer - class for performance of the task with some time
  interval
        * *checkInTime* - do task with some time interval
        * *checkInTimeWithSenderTypes* - do task with some time interval and
    it set one o more of SenderTypes
      * SenderByTime - speciality class for sending notification by time
          * *sendNotificationByTime* - send notification with some time interval. 
            * 1. It check if notification date equals with current date.
            * 2. If notification is ready to send it send it and put given notification
        to speciality list for already sending notifications for clear it after a while
          * *sendNotificationAndDistributeNotificationForSentLists* - it call method *send* from Sender and
        distribute sent notifications by sent lists with method *notificationSenderDistribute*
          * *timeChecker* - check if notification time (hours and minutes) equals
        with current date time (hours and minutes)
          * *notificationSenderDistribute* - distribute sent notifications by lists of sent
        notifications
          * *clearSentLists* - it clear lists for already sent notifications
          * *separateTimeFromDate* - separate only hours and minutes from date
          * *filterToClear* - return list of only ready to delete notifications
        by date
* ***util***
    * FromStringToDateParser - utility class contains methods for parsing 
  Date from String
      * *parse* - parse whole Date from String
      * *parseTime* - parse only part with hours and minutes but without year and month
      * *parseDateWithoutTime* - parse only part with year and month but without 
      hours and minutes
    * PrintNotificationToConsole - utility class to print notification
  information to console
      * *printNotificationToConsole* - print id, title, payload and date from
      notification to console
    * Time - utility class for working with date
      * *getCurrentTime* - return current time as String in "yyyy-MM-dd HH:mm" format
      * *timeComparator* - compare current date (only hours and minutes) and
      date (hours and minutes) from notification. It return true only if current time
      more than notification time no less then 2 minutes
  
          
  



  