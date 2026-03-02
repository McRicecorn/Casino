package com.example.casino.casino.Utility;

import org.springframework.http.HttpStatus;

public enum ErrorWrapper {
    SLOT_MODEL_INVALID_BET_AMOUNT("Der Einsatz muss größer als 0 sein.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("Der angegebene User existiert nicht.", HttpStatus.NOT_FOUND),
    INSUFFICIENT_BALANCE("Nicht genug Guthaben auf dem Konto.", HttpStatus.PAYMENT_REQUIRED),
    UNEXPECTED_INTERNAL_ERROR("Ein unerwarteter Fehler ist aufgetreten.", HttpStatus.INTERNAL_SERVER_ERROR);


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
