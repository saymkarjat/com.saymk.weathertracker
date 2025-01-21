package com.example.pogodaspring.weatherapi;

import com.example.pogodaspring.config.TestSpringConfig;
import com.example.pogodaspring.dto.SignUpUserDTO;
import com.example.pogodaspring.exception.LocationUndefinedException;
import com.example.pogodaspring.exception.LocationWeatherUndefinedException;
import com.example.pogodaspring.repository.UserRepository;
import com.example.pogodaspring.service.SignUpService;
import com.example.pogodaspring.weather.dto.LocationDTO;
import com.example.pogodaspring.weather.dto.response.GeoResponseDTO;
import com.example.pogodaspring.weather.dto.response.WeatherResponseDTO;
import com.example.pogodaspring.weather.repository.LocationRepository;
import com.example.pogodaspring.weather.service.LocationService;
import com.example.pogodaspring.weather.service.OpenWeatherApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Cleanup;
import lombok.SneakyThrows;
import net.minidev.json.writer.JsonReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ActiveProfiles("test")
@SpringJUnitConfig(classes = TestSpringConfig.class)
class LocationServiceTest {

    @MockitoBean
    OpenWeatherApiService openWeatherApiService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private LocationService locationService;


    @BeforeAll

    static void init(@Autowired SignUpService signUpService) {
        SignUpUserDTO user = SignUpUserDTO.builder()
                .username("vasya")
                .password("12345678")
                .confirmPassword("12345678")
                .build();
        signUpService.signUp(user);
    }

    @BeforeEach
    @SneakyThrows
    void setUp() {
        List<GeoResponseDTO> responseList = List.of(
                new GeoResponseDTO("Moscow", new BigDecimal("55.7504461"), new BigDecimal("37.6174943"), "RU", "Moscow"));
        Mockito.when(openWeatherApiService.getLocationsByName("Moscow", 1))
                .thenReturn(responseList);
        @Cleanup
        InputStream json = JsonReader.class.getResourceAsStream("/responseWeather.json");
        WeatherResponseDTO responseDTO = objectMapper.readValue(json, WeatherResponseDTO.class);
        Mockito.when(openWeatherApiService.getWeatherInfoByCoordinate(
                ArgumentMatchers.any(BigDecimal.class),
                ArgumentMatchers.any(BigDecimal.class),
                ArgumentMatchers.eq("metric"))
        ).thenReturn(responseDTO);
    }

    @Test
    void shouldReturnLocationsByCityName() {
        List<GeoResponseDTO> locations = locationService.findLocationsByName("Moscow", 1);
        GeoResponseDTO dto = locations.get(0);
        assertThat(dto.getCity()).isEqualTo("Moscow");
        assertThat(dto.getCountry()).isEqualTo("RU");

        List<GeoResponseDTO> emptyResult = locationService.findLocationsByName("Paris", 1);
        Assertions.assertTrue(emptyResult.isEmpty());
    }

    @Test
    void shouldAddLocationSuccessfully() {
        locationService.addNewLocation("55.7504461", "37.6174943", "Moscow", "vasya");

        assertThat(locationService.getRequiredList(5, 0, "vasya").size()).isEqualTo(1L);
        assertThat(locationService.getRequiredList(5, 0, "vasya").get(0).getName()).isEqualTo("Moscow");
    }
    @Test
    void shouldUpdateWeatherInfoForLocation(){
        locationService.addNewLocation("55.7504461", "37.6174943", "Moscow", "vasya");
        List<LocationDTO> requiredList = locationService.getRequiredList(5, 0, "vasya");
        assertThat(requiredList.get(0).getWeatherResponseDTO()).isNull();
        List<LocationDTO> updatedList = locationService.updateWeatherInfo(requiredList, "metric");

        assertThat(updatedList.get(0).getWeatherResponseDTO().getWeatherInfo().getTemp()).isEqualTo(locationService.getWeatherInfoByCoordinate(new BigDecimal("55.7504461"), new BigDecimal("37.6174943"), "metric").getWeatherInfo().getTemp());
    }
    @Test
    void shouldHandleWeatherApiExceptionsGracefully(){
        Mockito.when(openWeatherApiService.getLocationsByName(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
                .thenThrow(new LocationUndefinedException(""));

        Mockito.when(openWeatherApiService.getWeatherInfoByCoordinate(ArgumentMatchers.any(BigDecimal.class),
                ArgumentMatchers.any(BigDecimal.class),
                ArgumentMatchers.eq("metric")))
                .thenThrow(new LocationWeatherUndefinedException());



        assertThatThrownBy(()->locationService.getWeatherInfoByCoordinate(new BigDecimal("55.7504461"), new BigDecimal("37.6174943"), "metric"))
                .isInstanceOf(LocationWeatherUndefinedException.class);

        assertThatThrownBy(()->locationService.findLocationsByName("Moscow", 1))
                .isInstanceOf(LocationUndefinedException.class);
    }

}