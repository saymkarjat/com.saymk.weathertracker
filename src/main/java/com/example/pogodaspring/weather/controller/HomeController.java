package com.example.pogodaspring.weather.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class HomeController {

    @GetMapping("home")
    public String home(HttpServletRequest request, Model model) {
        String username = (String) request.getAttribute("username");
        Boolean isUserAuthenticated = (Boolean) request.getAttribute("isUserAuthenticated");
        model.addAttribute("username", username);
        model.addAttribute("isUserAuthenticated", isUserAuthenticated);
        return "home";
    }
}