package com.example.demo.exception;

public class DriverAlreadyPresentException extends RuntimeException {
    public DriverAlreadyPresentException(String message) {
        super(message);
    }

}
