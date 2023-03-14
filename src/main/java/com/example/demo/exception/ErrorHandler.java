package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DriverAlreadyPresentException.class)
    public ResponseEntity<ApplicationError> handleDriverAlreadyPresentException(DriverAlreadyPresentException exception, WebRequest webRequest) {
        ApplicationError error = new ApplicationError();
        error.setMessage(exception.getMessage());
        error.setCode(HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<ApplicationError> handleDriverNotFoundException(DriverNotFoundException exception, WebRequest webRequest) {
        ApplicationError error = new ApplicationError();
        error.setMessage(exception.getMessage());
        error.setCode(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VerificationPendingException.class)
    public ResponseEntity<ApplicationError> handleVerificationPendingException(VerificationPendingException exception, WebRequest webRequest) {
        ApplicationError error = new ApplicationError();
        error.setMessage(exception.getMessage());
        error.setCode(HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(VehicleDetailsMissingException.class)
    public ResponseEntity<ApplicationError> handleVVehicleDetailsMissingException(VehicleDetailsMissingException exception, WebRequest webRequest) {
        ApplicationError error = new ApplicationError();
        error.setMessage(exception.getMessage());
        error.setCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


}


