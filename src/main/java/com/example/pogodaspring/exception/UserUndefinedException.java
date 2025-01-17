package com.example.pogodaspring.exception;

public class UserUndefinedException extends RuntimeException{
    public UserUndefinedException(String message) {
        super(message);
    }
}
