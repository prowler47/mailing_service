package ua.dragunovskiy.mailing_service.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.util.NotificationTestUtil;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
public class NotificationDaoTests {

    @Autowired
    private NotificationDao notificationDao;

    @AfterEach
    public void cleanData() {
        Notification testNotification = notificationDao.getByTitle(NotificationTestUtil.getTestTitle());
        if (testNotification != null) {
            notificationDao.deleteByTitle(NotificationTestUtil.getTestTitle());
        }
    }

    @Test
    @DisplayName("Test save notification functionality")
    public void givenNotificationObject_whenAddForTesting_thenNotificationIsCreated() {
        //given
        Notification notification = NotificationTestUtil.createTestNotification();
        //when
        Notification savedNotification = notificationDao.save(notification);
        //then
        assertThat(savedNotification).isNotNull();
        UUID savedNotificationId = notificationDao.getByTitle(NotificationTestUtil.getTestTitle()).getId();
        assertThat(savedNotificationId).isNotNull();
    }

    @Test
    @DisplayName("Test update notification functionality")
    public void givenNotificationToUpdate_whenAddForTesting_thenEmailIsChange() {
        //given
        String updatedEmail = "testemail@gmail.com";
        Notification notificationToCreate = NotificationTestUtil.createTestNotification();
        Notification savedNotification = notificationDao.save(notificationToCreate);
        //when
        Notification notificationToUpdate = notificationDao.getByTitle(savedNotification.getTitle());
        notificationToUpdate.setAddress(updatedEmail);
        Notification updatedNotification = notificationDao.save(notificationToUpdate);
        //then
        assertThat(updatedNotification).isNotNull();
        assertThat(updatedNotification.getAddress()).isEqualTo(updatedEmail);
    }

    @Test
    @DisplayName("Test get by id notification functionality")
    public void givenNotificationCreated_whenGetById_thenNotificationIsReturned() {
        //given
        Notification notificationToCreate = NotificationTestUtil.createTestNotification();
        Notification savedNotification = notificationDao.save(notificationToCreate);
        //when
        Notification notificationByTitle = notificationDao.getByTitle(savedNotification.getTitle());
        Notification obtainedNotification = notificationDao.getById(notificationByTitle.getId());
        //then
        assertThat(obtainedNotification).isNotNull();
        assertThat(obtainedNotification.getAddress()).isEqualTo(notificationToCreate.getAddress());
    }

    @Test
    @DisplayName("Test get by title functionality")
    public void givenNotificationCreated_whenGetByTitle_thenNotificationIsReturned() {
        //given
        Notification notificationToCreate = NotificationTestUtil.createTestNotification();
        Notification savedNotification = notificationDao.save(notificationToCreate);
        //when
        Notification obtainedNotification = notificationDao.getByTitle(savedNotification.getTitle());
        //then
        assertThat(obtainedNotification).isNotNull();
        assertThat(obtainedNotification.getAddress()).isEqualTo(savedNotification.getAddress());
    }

    @Test
    @DisplayName("Test notification not found functionality")
    public void givenNotificationIsNotCreated_whenGetById_thenReturnNull() {
        //give

        //when
        Notification obtainedNotification = notificationDao.getById(UUID.fromString("e8f62b10-f370-4a3a-a0c9-1a242b97fc00"));
        //then
        assertThat(obtainedNotification).isNull();
    }

    @Test
    @DisplayName("Test get all notifications functionality")
    public void givenThreeNotificationsAreStored_whenGetAllForTesting_thenAllAreReturned() {
        //give
        List<Notification> listOfTestNotifications = NotificationTestUtil.createListOfTestNotifications();
        for (Notification notification : listOfTestNotifications) {
            notificationDao.save(notification);
        }
        //when
        List<Notification> obtainedNotifications = notificationDao.getAllForTesting("Title for testing mailing service");
        //then
        assertThat(CollectionUtils.isEmpty(obtainedNotifications)).isFalse();
    }


    @Test
    @DisplayName("Test delete by title functionality")
    public void givenNotificationCreated_whenDeleteByTitle_thenReturnNull() {
        //give
        Notification notificationToCreate = NotificationTestUtil.createTestNotification();
        Notification savedNotification = notificationDao.save(notificationToCreate);
        //when
        notificationDao.deleteByTitle(savedNotification.getTitle());
        //then
        assertThat(notificationDao.getByTitle(savedNotification.getTitle())).isNull();
    }

    @Test
    @DisplayName("Test delete by id functionality")
    public void givenNotificationCreated_whenDeleteById_thenReturnNull() {
        //given
        Notification notificationToCreate = NotificationTestUtil.createTestNotification();
        Notification savedNotification = notificationDao.save(notificationToCreate);
        //when
        UUID obtainedNotificationId = notificationDao.getByTitle(savedNotification.getTitle()).getId();
        notificationDao.delete(obtainedNotificationId);
        //then
        assertThat(notificationDao.getById(obtainedNotificationId)).isNull();
    }
}
