package com.example.pogodaspring.weather.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/app")
public class HomeController {

    @GetMapping("home")
    public String home(@RequestAttribute(name = "username", required = false) String username,
                       @RequestAttribute(name = "isUserAuthenticated") Boolean isUserAuthenticated,
                       Model model) {
        model.addAttribute("username", username);
        model.addAttribute("isUserAuthenticated", isUserAuthenticated);
        return "home";
    }
}