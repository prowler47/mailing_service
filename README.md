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
    * **comporator**
      * DateComparator
    * **filter**
      * Filter
      * SimpleFilterNotifications
    * **timer**
      * TaskByTimer
* **util**
    * FromStringToDateParser
    * PrintNotificationToConsole
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
        * send - method for send some message to client
    * MailSender - implementation of Sender for work with email
      * send - send message to email
    * TelegramSender - implementation of Sender for work with Telegram
        * send - send message to Telegram
    * ViberSender - implementation of Sender for work with Viber
        * send - send message to Viber
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
          
  



  