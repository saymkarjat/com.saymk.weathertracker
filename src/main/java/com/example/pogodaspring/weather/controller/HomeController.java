package com.example.pogodaspring.weather.controller;


import com.example.pogodaspring.weather.dto.LocationDTO;
import com.example.pogodaspring.weather.dto.response.GeoResponseDTO;
import com.example.pogodaspring.weather.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/app")
public class HomeController {

    private final int size = 5;
    private final String units = "metric";
    private LocationService locationService;

    public HomeController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("home")
    public String home(@RequestAttribute(name = "username", required = false) String username,
                       @RequestAttribute(name = "isUserAuthenticated") Boolean isUserAuthenticated,
                       @RequestParam(name = "page", defaultValue = "0") int page,
                       Model model) {
        model.addAttribute("username", username);
        model.addAttribute("isUserAuthenticated", isUserAuthenticated);
        List<LocationDTO> updatedLocations;
        if (isUserAuthenticated) {
            updatedLocations = locationService.updateWeatherInfo(locationService.getRequiredList(size, page, username), units);
            model.addAttribute("totalCount", Math.ceil((double) locationService.countLocationsByUsername(username) / size));
            model.addAttribute("locations", updatedLocations);
            model.addAttribute("page", page);
        }
        return "home";
    }

    @PostMapping("search")
    public String search(@RequestAttribute(name = "username") String username,
                         @RequestAttribute(name = "isUserAuthenticated") Boolean isUserAuthenticated,
                         @RequestParam(name = "location") String location,
                         Model model) {
        if (!isUserAuthenticated) {
            return "redirect:/auth/logout";
        }
        model.addAttribute("isUserAuthenticated", true);
        model.addAttribute("username", username);
        List<GeoResponseDTO> locations = locationService.findLocationsByName(location, 5);
        model.addAttribute("locations", locations);
        return "search";
    }
}