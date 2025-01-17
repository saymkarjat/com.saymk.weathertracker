package com.example.pogodaspring.exception;

public class LocationUndefinedException extends RuntimeException {
    public LocationUndefinedException(String message) {
        super(message);
    }
}
