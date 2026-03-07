package com.example.casino.casino.utility;

import org.springframework.http.HttpStatus;

public enum ErrorWrapper {
    INVALID_BET_AMOUNT("The stake must be greater than 0.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("The specified user does not exist.", HttpStatus.NOT_FOUND),
    INSUFFICIENT_BALANCE("Insufficient funds in your account.", HttpStatus.PAYMENT_REQUIRED),
    UNEXPECTED_INTERNAL_ERROR("An unexpected error has occurred.", HttpStatus.INTERNAL_SERVER_ERROR),
    GAME_NOT_FOUND("No corresponding game with the ID found.", HttpStatus.NOT_FOUND);



    private final String message;
    private final HttpStatus httpStatus;

    ErrorWrapper(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
