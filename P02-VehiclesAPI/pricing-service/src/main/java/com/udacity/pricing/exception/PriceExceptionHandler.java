package com.udacity.pricing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PriceExceptionHandler {
    @ExceptionHandler(PriceException.class)
    public ResponseEntity handlePriceException() {
        return new ResponseEntity("Price ID is not found.", HttpStatus.NOT_FOUND);
    }
}
