package ua.dragunovskiy.mailing_service.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ua.dragunovskiy.mailing_service.dto.NotificationDto;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.security.service.AuthService;
import ua.dragunovskiy.mailing_service.security.storage.SimpleUserNameStorage;
import ua.dragunovskiy.mailing_service.security.util.JwtTokenUtil;
import ua.dragunovskiy.mailing_service.service.NotificationService;
import ua.dragunovskiy.mailing_service.timemechanism.check.SimpleCheckNotifications;
import ua.dragunovskiy.mailing_service.timemechanism.filter.SimpleFilterNotifications;
import ua.dragunovskiy.mailing_service.util.NotificationDtoTestUtil;
import ua.dragunovskiy.mailing_service.util.NotificationTestUtil;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        ResultActions result = mockMvc.perform(post("/notifications/saveNotification")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title", is("test title")));
    }

    @Test
    @DisplayName("Test getAllNotificationDto functionality")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void givenListOfNotificationDto_whenGetAllNotificationDto_thenResponseSuccess() throws Exception {
        //give
        List<NotificationDto> listOfNotificationDto = NotificationDtoTestUtil.createListOfNotificationDto();
        BDDMockito.given(notificationService.getAllNotificationDto()).willReturn(listOfNotificationDto);
        //when
        ResultActions result = mockMvc.perform(get("/notifications/getAllNotificationDto")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listOfNotificationDto)));
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(3)));
    }
}
