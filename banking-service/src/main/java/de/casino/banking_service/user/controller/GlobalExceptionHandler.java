package de.casino.banking_service.user.controller;

import de.casino.banking_service.user.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
//@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler({
            InvalidAmountException.class,
            InvalidUserDataException.class
    })
    public ResponseEntity<Void> handleValidationErrors() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Void> handleNumberFormat() {
        return ResponseEntity.badRequest().build();
    }
}