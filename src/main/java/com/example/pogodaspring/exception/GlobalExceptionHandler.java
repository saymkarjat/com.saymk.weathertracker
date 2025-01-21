package com.example.pogodaspring.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            LocationUndefinedException.class,
            LocationWeatherUndefinedException.class,
            UserUndefinedException.class,
            WeatherApiException.class
    })
    public String handleCustomExceptions() {
        return "error-page";
    }

    @ExceptionHandler(Exception.class)
    public String handleAllOtherExceptions() {
        return "error-page";
    }

}