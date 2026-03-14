package de.casino.banking_service.user.controller;

import de.casino.banking_service.exceptions.InvalidAmountException;
import de.casino.banking_service.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<Void> handleInvalidAmount() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgument() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Void> handleNumberFormat() {
        return ResponseEntity.badRequest().build();
    }
}