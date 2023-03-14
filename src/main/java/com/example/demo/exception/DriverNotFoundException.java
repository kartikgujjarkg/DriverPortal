package com.example.demo.exception;

public class DriverNotFoundException extends RuntimeException {
    public DriverNotFoundException() {
        super("Driver not found");
    }
}
