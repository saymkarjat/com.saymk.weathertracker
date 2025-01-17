package com.example.pogodaspring.weather.dto;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record LocationDTO(
        int id,
        String name,
        int userId,
        BigDecimal lat,
        BigDecimal lon,
        WeatherResponseDTO weatherResponseDTO) {

}
