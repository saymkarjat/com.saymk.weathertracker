package com.example.pogodaspring.weather.controller;

import com.example.pogodaspring.weather.dto.LocationDTO;
import com.example.pogodaspring.weather.service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/app")
public class HomeController {
    private LocationService locationService;
    private boolean isCelsius = true;
    public HomeController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("home")
    public String home(@RequestAttribute(name = "username", required = false) String username,
                       @RequestAttribute(name = "isUserAuthenticated") Boolean isUserAuthenticated,
                       Model model) {
        model.addAttribute("username", username);
        model.addAttribute("isUserAuthenticated", isUserAuthenticated);
        String celsius = "metric";
        List<LocationDTO> updatedLocations = new ArrayList<>();
        if (isUserAuthenticated) {
            List<LocationDTO> userLocations = locationService.getUserLocations(username);
            updatedLocations = locationService.updateWeatherInfo(userLocations, celsius);
        }
        model.addAttribute("locationsList", updatedLocations);
        return "home";
    }
}