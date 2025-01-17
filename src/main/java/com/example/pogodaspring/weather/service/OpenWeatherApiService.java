package com.example.pogodaspring.weather.service;

import com.example.pogodaspring.weather.dto.GeoResponseDTO;
import com.example.pogodaspring.weather.dto.WeatherResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OpenWeatherApiService {
    private final String BASE_URL = "https://api.openweathermap.org";
    private final String API_KEY = "468ae6faa04106a9b2f60ca9774b305e";
    private ObjectMapper objectMapper;
    private WebClient webClient;

    @Autowired
    public OpenWeatherApiService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder
                .baseUrl(BASE_URL) // Базовый URL для всех запросов
                .build();
        this.objectMapper = objectMapper;
    }

    public Optional<String> fetchLocationsByName(String name, int limit) {
        return webClient.get().uri(uri -> uri
                        .path("/geo/1.0/direct")
                        .queryParam("q", name)
                        .queryParam("limit", limit)
                        .queryParam("appid", API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional();
    }

    public String fetchLocationWeatherByCoordinate(BigDecimal latitude, BigDecimal longitude, String units) {
        return webClient.get().uri(uri -> uri
                        .path("/data/2.5/weather")
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .queryParam("appid", API_KEY)
                        .queryParam("units", units)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }
    @SneakyThrows
    public List<GeoResponseDTO> getLocationsByName(String name, int limit) {
        Optional<String> locationsByName = fetchLocationsByName(name, limit);
        if (locationsByName.isEmpty()){
            return new ArrayList<>();
        }
        return objectMapper.readValue(locationsByName.get(), new TypeReference<List<GeoResponseDTO>>() {
        });
    }

    @SneakyThrows
    public WeatherResponseDTO getWeatherInfoByCoordinate(BigDecimal latitude, BigDecimal longitude, String units) {
        String string = fetchLocationWeatherByCoordinate(latitude, longitude, units);
        return objectMapper.readValue(string, WeatherResponseDTO.class);
    }


}
