package com.example.pogodaspring.weather.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
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
    @JsonProperty("sys")
    private Sys sys;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherInfo {
        // Геттеры
        @JsonProperty("temp")
        private int temp;

        @JsonProperty("feels_like")
        private int feelsLike;

        @JsonProperty("humidity")
        private int humidity;

        @JsonSetter("temp")
        public void setTemp(double temp) {
            this.temp = (int) Math.round(temp);
        }

        @JsonSetter("feels_like")
        public void setFeelsLike(double feelsLike) {
            this.feelsLike = (int) Math.round(feelsLike);
        }

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
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sys {

        @JsonProperty("country")
        private String country;


    }
}
