package com.example.pogodaspring.weather.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectToHomeController {
    @GetMapping("/")
    public String redirectToHomePage() {
        return "redirect:/app/home";
    }
}
