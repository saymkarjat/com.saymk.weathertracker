package com.example.pogodaspring.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponseDTO {
    @JsonProperty("main")
    private WeatherInfo weatherInfo;

    @JsonProperty("weather")
    private Weather[] weather;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherInfo {
        // Геттеры
        @JsonProperty("temp")
        private double temp;

        @JsonProperty("feels_like")
        private double feelsLike;

        @JsonProperty("humidity")
        private int humidity;

    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {

        @JsonProperty("main")
        private String mainWeather;

        @JsonProperty("description")
        private String description;

    }
}
