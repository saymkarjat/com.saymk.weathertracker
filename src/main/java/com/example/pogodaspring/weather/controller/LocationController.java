package com.example.pogodaspring.weather.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/app/location")
public class LocationController {
    private final int limit = 5;

    @PostMapping("search")
    public String home(@RequestParam String locationName, Model model) {

        return "home";
    }
}
