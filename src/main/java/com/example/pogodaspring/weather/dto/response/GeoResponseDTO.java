package com.example.pogodaspring.weather.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoResponseDTO {
    @JsonProperty("name")
    private String city;
    @JsonProperty("lat")
    private BigDecimal lat;
    @JsonProperty("lon")
    private BigDecimal lon;
    @JsonProperty("country")
    private String country;
    @JsonProperty("state")
    private String state;
}
