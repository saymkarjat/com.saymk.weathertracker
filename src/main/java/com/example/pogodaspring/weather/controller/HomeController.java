package com.example.pogodaspring.weather.controller;

import com.example.pogodaspring.model.Location;
import com.example.pogodaspring.model.User;
import com.example.pogodaspring.weather.dto.GeoResponseDTO;
import com.example.pogodaspring.weather.dto.LocationDTO;
import com.example.pogodaspring.weather.dto.LocationMapper;
import com.example.pogodaspring.weather.service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/app")
public class HomeController {
    private LocationService locationService;
    LocationMapper mapper;
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
        //todo
        List<LocationDTO> updatedLocations = new ArrayList<>();
        if (isUserAuthenticated) {
            List<LocationDTO> userLocations = locationService.getUserLocations(username);
            updatedLocations = locationService.updateWeatherInfo(userLocations, celsius);
        }
        model.addAttribute("locations", updatedLocations);
        return "home";
    }
    @PostMapping("search")
    public String search(@RequestAttribute(name = "username") String username,
                         @RequestAttribute(name = "isUserAuthenticated") Boolean isUserAuthenticated,
                         @RequestParam(name = "location") String location,
                         Model model){
        if (!isUserAuthenticated){
            return "redirect:/auth/logout";
        }
        model.addAttribute("isUserAuthenticated", true);
        model.addAttribute("username", username);
        List<GeoResponseDTO> locations = locationService.findLocationsByName(location, 5);
        model.addAttribute("locations", locations);
        return "search";
    }
}