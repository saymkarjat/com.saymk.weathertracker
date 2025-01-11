package com.example.pogodaspring.controller;

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
        model.addAttribute("username", request.getAttribute("username"));
        model.addAttribute("isUserAuthenticated", request.getAttribute("isUserAuthenticated"));
        return "home";
    }
}