package com.example.demo.exception;

public class VehicleDetailsMissingException extends RuntimeException {

    public VehicleDetailsMissingException() {
        super("Vehicle details missing");
    }
}
