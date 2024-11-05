package ua.dragunovskiy.mailing_service.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.entity.User;
import ua.dragunovskiy.mailing_service.service.NotificationService;
import ua.dragunovskiy.mailing_service.util.FromDatetimeLocalToStringParser;

@Controller
@RequestMapping("/view")
@RequiredArgsConstructor
public class ViewController {

    private final NotificationService notificationService;


    @GetMapping("/done")
    public String itsDonePage() {
        return "itsDone";
    }

    @GetMapping("/registrationSuccess")
    public String registrationSuccessPage() {
        return "registration_success";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccessPage() {
        return "login_success";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/registration")
    public String userRegistration(Model model) {
        User user = new User();
        model.addAttribute("newUser", user);
        return "registration";
    }

    @PostMapping("/registerUser")
    public String userRegistration(@ModelAttribute("newUser") User user) {
        return "redirect:/view/registrationSuccess";
    }

    @GetMapping("/login")
    public String userLogin(Model model) {
        User user = new User();
        model.addAttribute("loginUser", user);
        return "login";
    }

    @PostMapping("/loginUser")
    public String userLogin(@ModelAttribute("loginUser") User user) {
        return "redirect:/view/loginSuccess";
    }
    @GetMapping("/newNotification")
    public String addNewNotification(Model model) {
        Notification notification = new Notification();
        model.addAttribute("newNotification", notification);
        return "addNotification";
    }

    @PostMapping("/addNotification")
    public String addNewNotification(@ModelAttribute("newNotification") Notification notification) {
//        System.out.println(FromDatetimeLocalToStringParser.parse(notification.getDate()));
        notification.setDate(FromDatetimeLocalToStringParser.parse(notification.getDate()));
        notificationService.addNewNotification(notification);
        return "redirect:/view/done";
    }


}
