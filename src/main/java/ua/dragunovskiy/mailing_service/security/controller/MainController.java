package ua.dragunovskiy.mailing_service.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/secured")
    public String secured() {
        return "Secured info";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Admin info";
    }
}
