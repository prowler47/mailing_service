package ua.dragunovskiy.mailing_service.ui;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.ModelAndView;
import ua.dragunovskiy.mailing_service.dto.NotificationDto;
import ua.dragunovskiy.mailing_service.dto.NotificationDtoWithID;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.security.dto.JwtRequestDto;
import ua.dragunovskiy.mailing_service.security.dto.RegistrationUserDto;
import ua.dragunovskiy.mailing_service.security.service.AuthService;
import ua.dragunovskiy.mailing_service.service.NotificationService;
import ua.dragunovskiy.mailing_service.util.FromDatetimeLocalToStringParser;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/view")
@RequiredArgsConstructor
public class ViewController {
    private final NotificationService notificationService;
    private final AuthService authService;

    @GetMapping("/done")
    public String itsDonePage() {
        return "itsDone";
    }

    @GetMapping("/info")
    public String info() { return "info"; }

    @GetMapping("/registrationSuccess")
    public String registrationSuccessPage() {
        return "registration_success";
    }

    @GetMapping("/passwordNotMatch")
    public String passwordNotMatch() {return "password_not_match"; }

    @GetMapping("/userAlreadyExist")
    public String userAlreadyExist() { return "user_already_exist";}

    @GetMapping("/emailAlreadyExist")
    public String emailAlreadyExist() { return "email_already_exist"; }

    @GetMapping("/loginSuccess")
    public String loginSuccessPage() {
        return "login_success";
    }

    @GetMapping("/loginError")
    public String loginError() {return "login_error"; }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/successLoginHome")
    public String successLoginUserHome() { return "home_login_user";}

    @GetMapping("/registration")
    public String userRegistration(Model model) {
        RegistrationUserDto user = new RegistrationUserDto();
        model.addAttribute("newUser", user);
        return "registration";
    }

    @PostMapping("/registerUser")
    public String userRegistration(@ModelAttribute("newUser") RegistrationUserDto user) {
        ResponseEntity<?> response = authService.createNewUser(user);
        if (response.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
            return "redirect:/view/userAlreadyExist";
        }
        if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            return "redirect:/view/passwordNotMatch";
        }
        if (response.getStatusCode().equals(HttpStatus.CONFLICT)) {
            return "redirect:/view/emailAlreadyExist";
        }
        System.out.println(user.getUsername());
        return "redirect:/view/registrationSuccess";
    }

    @GetMapping("/login")
    public String userLogin(Model model) {
        JwtRequestDto jwtRequestDto = new JwtRequestDto();
        model.addAttribute("loginUser", jwtRequestDto);
        return "login";
    }

    @PostMapping("/loginUser")
    public String userLogin(@ModelAttribute JwtRequestDto jwtRequestDto, HttpServletResponse response) {
        System.out.println(jwtRequestDto.getUsername());
        ResponseEntity<?> authToken = authService.createAuthToken(jwtRequestDto);
        if (authToken.getStatusCode().is4xxClientError()) {
            return "redirect:/view/loginError";
        }
        String tokenSubstring = authToken.getBody().toString().substring(21);
        String token = tokenSubstring.substring(0, tokenSubstring.length() - 1);
        Cookie cookie = new Cookie("jwt", token);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        response.setContentType("text/plain");
        return "redirect:/view/userPage";
    }

    @GetMapping("/userPage")
    public String userPage() { return "user_page"; }

    @GetMapping("/newNotification")
    public String addNewNotification(Model model) {
        Notification notification = new Notification();
        model.addAttribute("newNotification", notification);
        return "addNotification";
    }

    @PostMapping("/addNotification")
    public String addNewNotification(@ModelAttribute("newNotification") Notification notification) {
        notification.setDate(FromDatetimeLocalToStringParser.parse(notification.getDate()));
        notificationService.addNewNotification(notification);
        return "redirect:/view/done";
    }

    @GetMapping("/updateNotification/{id}")
    public String updateNotification(Model model, @PathVariable("id") UUID id) {
        Notification notificationForUpdate = notificationService.getNotificationById(id);
        model.addAttribute("notificationForUpdate", notificationForUpdate);
        return "updateNotification";
    }

    @PostMapping("/updateNotification")
    public String updateNotification(@ModelAttribute("notificationForUpdate") Notification notificationForUpdate) {
        notificationService.updateNotification(notificationForUpdate.getId(), notificationForUpdate);
        return "redirect:/view/getAllNotificationsByUsername";
    }


    // use notification dto without id

//    @GetMapping("/getAllNotificationsByUsername")
//    public String printAllNotificationsByUsername(Model model) {
//        List<NotificationDto> notifications = notificationService.getAllNotificationsByUsernameFromCookie();
//        model.addAttribute("notifications", notifications);
//        return "notificationsByUsername";
//    }

    // use notification dto with id
    @GetMapping("/getAllNotificationsByUsername")
    public String printAllNotificationsByUsername(Model model) {
        List<NotificationDtoWithID> notifications = notificationService.getAllNotificationDtoWithIDByUsernameFromCookies();
        model.addAttribute("notifications", notifications);
        return "notificationsByUsername";
    }

//    @GetMapping("/getAllNotificationsByUsername")
//    public ModelAndView printAllNotificationsByUsername(Model model) {
//        List<NotificationDtoWithID> notifications = notificationService.getAllNotificationDtoWithIDByUsernameFromCookies();
//        model.addAttribute("notifications", notifications);
//        return new ModelAndView("notificationsByUsername");
//    }


    @PostMapping("/deleteNotification")
    public String deleteNotification(@RequestParam UUID id) {
        notificationService.deleteNotification(id);
        return "redirect:/view/getAllNotificationsByUsername";
    }
}
