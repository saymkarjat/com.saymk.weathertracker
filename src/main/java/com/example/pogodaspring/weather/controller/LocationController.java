package com.example.pogodaspring.weather.controller;


import com.example.pogodaspring.weather.service.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/app/location")
@AllArgsConstructor
public class LocationController {
    private final int limit = 5;
    private LocationService locationService;

    @PostMapping("add")
    public String add(@RequestAttribute(name = "username") String username,
                      @RequestAttribute(name = "isUserAuthenticated") Boolean isUserAuthenticated,
                          @RequestParam(name = "latitude") String latitude,
                      @RequestParam(name = "longitude") String longitude,
                      @RequestParam(name = "city") String city) {
        if (!isUserAuthenticated){
            return "redirect:/auth/logout";
        }
        locationService.addNewLocation(latitude, longitude, city, username);
        return "redirect:/app/home";
    }

    @PostMapping("delete")
    public String delete(@RequestAttribute(name = "username") String username,
                      @RequestAttribute(name = "isUserAuthenticated") Boolean isUserAuthenticated,
                      @RequestParam(name = "locationId") String locationId) {
        if (!isUserAuthenticated){
            return "redirect:/auth/logout";
        }
        locationService.deleteLocationById(Integer.parseInt(locationId), username);
        return "redirect:/app/home";
    }


}
