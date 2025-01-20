package ua.dragunovskiy.mailing_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.dragunovskiy.mailing_service.dto.NotificationDtoWithID;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.exception.OverdueMessageException;
import ua.dragunovskiy.mailing_service.exception.UsernameFromCookieNotFound;
import ua.dragunovskiy.mailing_service.mapper.NotificationDtoWithIDMapper;
import ua.dragunovskiy.mailing_service.repository.NotificationDao;
import ua.dragunovskiy.mailing_service.security.storage.SimpleUserNameStorage;
import ua.dragunovskiy.mailing_service.util.NotificationTestUtil;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @Mock
    private NotificationDao notificationDao;
    @Mock
    private EncryptionService encryptionService;
    @Mock
    private SimpleUserNameStorage userNameStorage;
    @Mock
    private NotificationDtoWithIDMapper notificationDtoWithIDMapper;
    @InjectMocks
    private NotificationService serviceUnderTest;

    @Test
    @DisplayName("Test save notification functionality")
    public void givenNotificationToSave_whenSaveNewNotification_thenRepositoryIsCalled() {
        //given
        Notification notificationToSave = NotificationTestUtil.createTestNotification();
        BDDMockito.given(notificationDao.save(any(Notification.class))).willReturn(NotificationTestUtil.createTestNotification());
        BDDMockito.given(userNameStorage.getUsernameFromStorage()).willReturn("test username");
        BDDMockito.given(encryptionService.encodeUsername(userNameStorage.getUsernameFromStorage())).willReturn("encoded username");
        //when
        Notification savedNotification = serviceUnderTest.saveNewNotification(notificationToSave);
        //then
        assertThat(savedNotification).isNotNull();
        verify(notificationDao).save(notificationToSave);
    }

    @Test
    @DisplayName("Test save notification with username is null functionality")
    public void givenNotificationToSave_whenSaveNewNotificationWithUsernameIsNull_thenExceptionIsThrows() {
        //given
        Notification notificationToSave = NotificationTestUtil.createTestNotification();
        BDDMockito.given(encryptionService.encodeUsername(userNameStorage.getUsernameFromStorage())).willReturn(null);
        //when
        assertThrows(UsernameFromCookieNotFound.class, () -> serviceUnderTest.saveNewNotification(notificationToSave));
        //then
        verify(notificationDao, never()).save(any(Notification.class));
    }

    @Test
    @DisplayName("Test update notification functionality")
    public void givenNotificationToUpdate_whenUpdateNotification_thenRepositoryIsCalled() {
        //given
        Notification notificationToUpdate = NotificationTestUtil.createTestNotification();
        Notification notificationForUpdate = NotificationTestUtil.createTestNotificationForUpdateNotificationTest();
        BDDMockito.given(notificationDao.update(any(UUID.class), any(Notification.class))).willReturn(notificationForUpdate);
        BDDMockito.given(notificationDao.getById(any(UUID.class))).willReturn(notificationToUpdate);
        //when
        Notification updatedNotification = serviceUnderTest.updateNotification(notificationToUpdate.getId(), notificationForUpdate);
        //then
        assertThat(updatedNotification).isNotNull();
        verify(notificationDao, times(1)).update(notificationToUpdate.getId(), notificationForUpdate);
    }

    @Test
    @DisplayName("Test update notification with overdue date functionality")
    public void givenNotificationToUpdateWithOverdueDate_whenUpdateNotification_thenExceptionIsThrows() {
        //given
        Notification notificationToUpdate = NotificationTestUtil.createTestNotificationWithOverdueDate();
        Notification notificationForUpdate = NotificationTestUtil.createTestNotificationForUpdateNotificationTest();
        BDDMockito.given(notificationDao.getById(any(UUID.class))).willReturn(notificationToUpdate);
        //when
        assertThrows(OverdueMessageException.class, () -> serviceUnderTest.updateNotification(notificationForUpdate.getId(), notificationForUpdate));
        //then
        verify(notificationDao, never()).update(any(UUID.class), any(Notification.class));
    }

    @Test
    @DisplayName("Test get notifications by username functionality")
    public void givenListOfNotifications_whenGetAllNotificationDtoWithIDByUsernameFromCookiesV2_thenRepositoryIsCalled() {
        //given
        List<Notification> listOfTestNotifications = NotificationTestUtil.createListOfTestNotifications();
        BDDMockito.given(notificationDao.getAll()).willReturn(listOfTestNotifications);
        BDDMockito.given(userNameStorage.getUsernameFromStorage()).willReturn("username");
        BDDMockito.given(encryptionService.encodeUsername(userNameStorage.getUsernameFromStorage())).willReturn("user for test");
        //when
        List<NotificationDtoWithID> notifications = serviceUnderTest.getAllNotificationDtoWithIDByUsernameFromCookiesV2();
        //then
        assertThat(notifications).isNotNull();
        assertThat(notifications.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test get notifications when username from cookie is null")
    public void givenUsernameFromCookieIsNull_whenGetAllNotificationDtoWithIDByUsernameFromCookieV2_thenExceptionIsThrows() {
        //given
        BDDMockito.given(userNameStorage.getUsernameFromStorage()).willReturn(null);
        BDDMockito.given(encryptionService.encodeUsername(userNameStorage.getUsernameFromStorage())).willReturn(null);
        //when
        assertThrows(UsernameFromCookieNotFound.class, () -> serviceUnderTest.getAllNotificationDtoWithIDByUsernameFromCookiesV2());
        //then
        verify(notificationDao, never()).getAll();
        verify(notificationDtoWithIDMapper, never()).map(any(Notification.class));
    }
}
