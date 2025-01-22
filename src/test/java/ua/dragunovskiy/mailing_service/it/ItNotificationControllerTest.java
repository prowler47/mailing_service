package ua.dragunovskiy.mailing_service.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ua.dragunovskiy.mailing_service.dto.NotificationDto;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.repository.NotificationDao;
import ua.dragunovskiy.mailing_service.util.NotificationDtoTestUtil;
import ua.dragunovskiy.mailing_service.util.NotificationTestUtil;
import ua.dragunovskiy.mailing_service.util.Time;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItNotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private NotificationDao notificationDao;

    @BeforeEach
    public void setUp() {
        notificationDao.deleteAll();
    }

    @Test
    @DisplayName("Test save notification functionality")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void givenNotificationDto_whenSave_thenResponseSuccess() throws Exception {
        //give
        Notification notification = NotificationTestUtil.createTestNotification();
        notificationDao.save(notification);
        Notification obtainedNotification = notificationDao.getByTitle("Title for testing mailing service");

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/notifications/notifications")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(obtainedNotification)));
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title", is("Title for testing mailing service")))
                .andExpect(jsonPath("$.address", is("max@mail.com")))
                .andExpect(jsonPath("$.date", is(Time.getCurrentTimePlusOneYear())))
                .andExpect(jsonPath("$.payload", is("test payload")));
    }

    @Test
    @DisplayName("Test getAllNotificationDto functionality")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void givenListOfNotification_whenGetAllNotification_thenResponseSuccess() throws Exception {
        //give
        List<Notification> listOfNotifications = NotificationTestUtil.createListOfTestNotifications();
        for (Notification notification : listOfNotifications) {
            notificationDao.save(notification);
        }
        //when
        ResultActions result = mockMvc.perform(get("/api/v1/notifications/notifications")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listOfNotifications)));
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(3)));
    }

    @Test
    @DisplayName("Test get notification by id functionality")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void givenNotificationId_whenGetNotificationDtoById_thenResponseSuccess() throws Exception {
        //given
        Notification notification = NotificationTestUtil.createTestNotification();
        notification.setDate(Time.getCurrentTime());
        notificationDao.save(notification);
        Notification obtainedNotification = notificationDao.getByTitle("Title for testing mailing service");
        //when
        ResultActions result = mockMvc.perform(get("/api/v1/notifications/notifications/" + obtainedNotification.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notification)));
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title", is("Title for testing mailing service")))
                .andExpect(jsonPath("$.address", is("max@mail.com")))
                .andExpect(jsonPath("$.date", is(Time.getCurrentTime())))
                .andExpect(jsonPath("$.payload", is("test payload")));
    }

    @Test
    @DisplayName("Test get notification by id with incorrect id functionality")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void givenIncorrectNotificationId_whenGetNotificationById_thenResponseError() throws Exception {
        //given
        NotificationDto notificationDto = NotificationDtoTestUtil.createNotificationDto();
        //when
        ResultActions result = mockMvc.perform(get("/api/v1/notifications/notifications/" + UUID.randomUUID())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationDto)));
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Notification with given id is not exists")));

    }

    @Test
    @DisplayName("Test notification update functionality")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void givenNotification_whenUpdateNotification_thenResponseSuccess() throws Exception {
        //give
        String updatedEmail = "updated@email.com";
        Notification notificationToUpdate = NotificationTestUtil.createTestNotification();
        notificationDao.save(notificationToUpdate);
        Notification obtainedNotification = notificationDao.getByTitle("Title for testing mailing service");
        Notification notificationForUpdate = NotificationTestUtil.createTestNotification();
        notificationForUpdate.setDate(Time.getCurrentTime());
        notificationForUpdate.setAddress(updatedEmail);
        //when
        ResultActions result = mockMvc.perform(put("/api/v1/notifications/notifications/" + obtainedNotification.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationForUpdate)));
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title", is("Title for testing mailing service")))
                .andExpect(jsonPath("$.address", is("updated@email.com")))
                .andExpect(jsonPath("$.date", is(Time.getCurrentTime())))
                .andExpect(jsonPath("$.payload", is("test payload")));
    }
    @Test
    @DisplayName("Test update notification with in incorrect notification id")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void givenIncorrectIdForUpdatedNotification_whenUpdate_thenResponseError() throws Exception {
        //give
        NotificationDto notificationDto = NotificationDtoTestUtil.createNotificationDto();
        //when
        ResultActions result = mockMvc.perform(put("/api/v1/notifications/notifications/" + UUID.randomUUID())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationDto)));
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Incorrect id for updated notification")));
    }

    @Test
    @DisplayName("Test update notification with overdue date")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void givenNotificationWithOverdueDate_whenUpdate_thenResponseError() throws Exception {
        //given
        Notification notificationToUpdate = NotificationTestUtil.createTestNotification();
        notificationToUpdate.setDate("2020-01-15 11:26");
        notificationDao.save(notificationToUpdate);
        Notification obtainedNotification = notificationDao.getByTitle("Title for testing mailing service");
        Notification notificationForUpdate = NotificationTestUtil.createTestNotification();
        //when
        ResultActions result = mockMvc.perform(put("/api/v1/notifications/notifications/" + obtainedNotification.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationForUpdate)));
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("This message is already send")));
    }

    @Test
    @DisplayName("Test delete notification by id functionality")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void givenNotificationId_whenDeleteNotification_thenResponseSuccess() throws Exception {
        //given
        Notification notification = NotificationTestUtil.createTestNotification();
        notificationDao.save(notification);
        Notification obtainedNotification = notificationDao.getByTitle("Title for testing mailing service");
        //when
        ResultActions result = mockMvc.perform(delete("/api/v1/notifications/notifications/" + obtainedNotification.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(obtainedNotification)));
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test delete notification by id with incorrect id")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void givenIncorrectNotificationId_whenDeleteNotification_thenResponseError() throws Exception {
        //give
        //when
        ResultActions result = mockMvc.perform(delete("/api/v1/notifications/notifications/" + UUID.randomUUID())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Incorrect notification id")));
    }
}
