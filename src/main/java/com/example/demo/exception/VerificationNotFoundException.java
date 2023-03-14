package com.example.demo.exception;

public class VerificationNotFoundException extends RuntimeException {
    public VerificationNotFoundException() {
        super("Verification task not found");
    }

}
