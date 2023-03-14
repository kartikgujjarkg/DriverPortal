package com.example.demo.exception;

public class VerificationPendingException extends RuntimeException {
    public VerificationPendingException(String message) {
        super(message);
    }
}
