package com.example.pogodaspring.weather.service;

import com.example.pogodaspring.exception.LocationUndefinedException;
import com.example.pogodaspring.exception.UserUndefinedException;
import com.example.pogodaspring.model.Location;
import com.example.pogodaspring.model.User;
import com.example.pogodaspring.repository.UserRepository;
import com.example.pogodaspring.weather.dto.GeoResponseDTO;
import com.example.pogodaspring.weather.dto.LocationDTO;
import com.example.pogodaspring.weather.dto.LocationMapper;
import com.example.pogodaspring.weather.dto.WeatherResponseDTO;
import com.example.pogodaspring.weather.repository.LocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class LocationService {
    private OpenWeatherApiService weatherApiService;
    private LocationRepository locationRepository;
    private UserRepository userRepository;
    private LocationMapper locationMapper;

    public LocationService(OpenWeatherApiService weatherApiService, LocationRepository locationRepository, UserRepository userRepository, LocationMapper locationMapper) {
        this.weatherApiService = weatherApiService;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.locationMapper = locationMapper;
    }

    public List<GeoResponseDTO> findLocationsByName(String name, int limit) {
        return weatherApiService.getLocationsByName(name, limit);
    }

    public WeatherResponseDTO getWeatherInfoByCoordinate(BigDecimal latitude, BigDecimal longitude, String units){
        return weatherApiService.getWeatherInfoByCoordinate(latitude, longitude, units);
    }

    @Transactional
    public void deleteLocationById(int id, String username) {
        Optional<User> optionalUser = userRepository.findByLogin(username);
        User user = optionalUser.get();
        Optional<Location> locationById = locationRepository.findLocationById(id);
        if (locationById.isEmpty()){
            log.error("Location undefined");
            throw new LocationUndefinedException("Location undefined");
        }
        user.getLocations().remove(locationById.get());
        locationRepository.removeLocation(locationById.get());
        userRepository.updateUser(user);
    }

    public List<LocationDTO> updateWeatherInfo(List<LocationDTO> locationsList, String units) {
        List<LocationDTO> updatedList = new ArrayList<>();
        for (LocationDTO location : locationsList) {
            location.setWeatherResponseDTO(getWeatherInfoByCoordinate(location.getLat(), location.getLon(), units));
            updatedList.add(location);
        }
        return updatedList;
    }

    @Transactional
    public void addNewLocation(String lat, String lon, String city, String username) {
        Optional<User> optionalUser = userRepository.findByLogin(username);
        if (optionalUser.isEmpty()){
            log.error("This user undefined: {}", username);
            throw new UserUndefinedException("This user undefined: "+username);
        }
        User user = optionalUser.get();
        Location location = Location.builder()
                .user(optionalUser.get())
                .latitude(new BigDecimal(lat))
                .longitude(new BigDecimal(lon))
                .name(city)
                .build();
        locationRepository.saveLocation(location);
        user.getLocations().add(location);
        userRepository.updateUser(user);
    }

    @Transactional(readOnly = true)
    public List<LocationDTO> getUserLocations(String username) {
        User user = userRepository.findByLogin(username).get();
        List<Location> locations = user.getLocations();
        return locationMapper.toDtoList(locations);
    }
}
