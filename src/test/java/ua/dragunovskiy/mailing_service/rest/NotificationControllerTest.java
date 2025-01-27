package ua.dragunovskiy.mailing_service.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ua.dragunovskiy.mailing_service.dto.NotificationDto;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.exception.IncorrectNotificationIdException;
import ua.dragunovskiy.mailing_service.exception.IncorrectUserIdException;
import ua.dragunovskiy.mailing_service.exception.OverdueMessageException;
import ua.dragunovskiy.mailing_service.security.service.AuthService;
import ua.dragunovskiy.mailing_service.security.storage.SimpleUserNameStorage;
import ua.dragunovskiy.mailing_service.security.util.JwtTokenUtil;
import ua.dragunovskiy.mailing_service.service.NotificationService;
import ua.dragunovskiy.mailing_service.timemechanism.check.SimpleCheckNotifications;
import ua.dragunovskiy.mailing_service.timemechanism.filter.SimpleFilterNotifications;
import ua.dragunovskiy.mailing_service.util.NotificationDtoTestUtil;
import ua.dragunovskiy.mailing_service.util.NotificationTestUtil;
import ua.dragunovskiy.mailing_service.util.Time;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest
public class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private SimpleCheckNotifications simpleCheckNotifications;
    @MockBean
    private SimpleFilterNotifications simpleFilterNotifications;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private AuthService authService;
    @MockBean
    private SimpleUserNameStorage simpleUserNameStorage;
    @MockBean
    private NotificationService notificationService;

    @Test
    @DisplayName("Test save notification functionality")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void givenNotificationDto_whenSave_thenResponseSuccess() throws Exception {
        //give
        NotificationDto dto = NotificationDtoTestUtil.createNotificationDto();
        BDDMockito.given(notificationService.save(any(Notification.class))).willReturn(dto);
        //when
        ResultActions result = mockMvc.perform(post("/api/v1/notifications/notifications")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title", is("test title")))
                .andExpect(jsonPath("$.address", is("test address")))
                .andExpect(jsonPath("$.date", is(Time.getCurrentTime())))
                .andExpect(jsonPath("$.payload", is("test payload")));
    }

    @Test
    @DisplayName("Test getAllNotificationDto functionality")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void givenListOfNotificationDto_whenGetAllNotificationDto_thenResponseSuccess() throws Exception {
        //give
        List<NotificationDto> listOfNotificationDto = NotificationDtoTestUtil.createListOfNotificationDto();
        BDDMockito.given(notificationService.getAllNotificationDto()).willReturn(listOfNotificationDto);
        //when
        ResultActions result = mockMvc.perform(get("/api/v1/notifications/notifications")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listOfNotificationDto)));
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
        NotificationDto notificationDto = NotificationDtoTestUtil.createNotificationDto();
        BDDMockito.given(notificationService.getNotificationDtoById(any(UUID.class))).willReturn(notificationDto);
        //when
        ResultActions result = mockMvc.perform(get("/api/v1/notifications/notifications/" + UUID.randomUUID())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationDto)));
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title", is("test title")))
                .andExpect(jsonPath("$.address", is("test address")))
                .andExpect(jsonPath("$.date", is(Time.getCurrentTime())))
                .andExpect(jsonPath("$.payload", is("test payload")));
    }

    @Test
    @DisplayName("Test get notification by id with incorrect id functionality")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void givenIncorrectNotificationId_whenGetNotificationById_thenResponseError() throws Exception {
        //given
        NotificationDto notificationDto = NotificationDtoTestUtil.createNotificationDto();
        BDDMockito.given(notificationService.getNotificationDtoById(any(UUID.class)))
                .willThrow(new IncorrectNotificationIdException("Notification with given id is not exists"));
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
        Notification notificationToUpdate = NotificationTestUtil.createTestNotification();
        NotificationDto notificationDto = NotificationDtoTestUtil.createNotificationDto();
        BDDMockito.given(notificationService.update(any(UUID.class), any(Notification.class))).willReturn(notificationDto);
        //when
        ResultActions result = mockMvc.perform(put("/api/v1/notifications/notifications/" + notificationToUpdate.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationDto)));
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title", is("test title")))
                .andExpect(jsonPath("$.address", is("test address")))
                .andExpect(jsonPath("$.date", is(Time.getCurrentTime())))
                .andExpect(jsonPath("$.payload", is("test payload")));
    }
    @Test
    @DisplayName("Test update notification with in incorrect notification id")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void givenIncorrectIdForUpdatedNotification_whenUpdate_thenResponseError() throws Exception {
        //give
        NotificationDto notificationDto = NotificationDtoTestUtil.createNotificationDto();
        BDDMockito.given(notificationService.update(any(UUID.class), any(Notification.class)))
                .willThrow(new IncorrectNotificationIdException("Incorrect id for updated notification"));
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
        NotificationDto notificationDto = NotificationDtoTestUtil.createNotificationDto();
        BDDMockito.given(notificationService.update(any(UUID.class), any(Notification.class)))
                .willThrow(new OverdueMessageException("This message is already send"));
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
                .andExpect(jsonPath("$.message", is("This message is already send")));
    }

    @Test
    @DisplayName("Test delete notification by id functionality")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void givenNotificationId_whenDeleteNotification_thenResponseSuccess() throws Exception {
        //given
        BDDMockito.doNothing().when(notificationService).deleteNotification(any(UUID.class));
        //when
        ResultActions result = mockMvc.perform(delete("/api/v1/notifications/notifications/" + UUID.randomUUID())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        verify(notificationService, times(1)).deleteNotification(any(UUID.class));
        result
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test delete notification by id with incorrect id")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void givenIncorrectNotificationId_whenDeleteNotification_thenResponseError() throws Exception {
        //give
       BDDMockito.willThrow(new IncorrectNotificationIdException("Incorrect notification id"))
               .given(notificationService).deleteNotification(any(UUID.class));
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
