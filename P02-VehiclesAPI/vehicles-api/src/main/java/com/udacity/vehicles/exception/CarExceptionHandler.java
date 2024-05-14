package com.udacity.vehicles.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;


@ControllerAdvice
public class CarExceptionHandler {
    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<ApiError> handleCarNotFoundException() {
        return new ResponseEntity<>(new ApiError("Car ID is not found", new ArrayList<>()), HttpStatus.NOT_FOUND);
    }
}
