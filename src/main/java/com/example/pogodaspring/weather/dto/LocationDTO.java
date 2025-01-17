package com.example.pogodaspring.weather.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
public class LocationDTO {
    private int id;
    private String name;
    private int userId;
    private BigDecimal lat;
    private BigDecimal lon;
    private WeatherResponseDTO weatherResponseDTO;
}
