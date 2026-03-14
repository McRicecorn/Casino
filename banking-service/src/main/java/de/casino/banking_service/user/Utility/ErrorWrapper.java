package de.casino.banking_service.user.Utility;

import org.springframework.http.HttpStatus;


public enum ErrorWrapper {

    //User related errors
    UNEXPECTED_INTERNAL_ERROR_OCCURED(
            "An unexpected internal error occurred.",
            HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(
            "A requested user could not be found.",
            HttpStatus.NOT_FOUND),
    USER_MODEL_INVALID_NAME_Blank_OR_NULL(
            "The first name and last name of a user must be non-empty.",
            HttpStatus.BAD_REQUEST),
    USER_MODEL_INVALID_NAME_LENGTH(
            "The first name and last name of a user must not contain more than 12 characters.",
            HttpStatus.BAD_REQUEST),

    USER_MODEL_IDENTICAL_NAME(
            "This is already your first and last name.",
            HttpStatus.BAD_REQUEST
    ),

    // Amount related errors
    USER_MODEL_INVALID_AMOUNT(
            "The amount must be greater than 0.",
            HttpStatus.BAD_REQUEST),
    USER_MODEL_INVALID_AMOUNT_NEGATIVE(
            "The amount must be a positive number.",
            HttpStatus.BAD_REQUEST),
    USER_MODEL_INVALID_AMOUNT_DECIMAL_PLACES(
            "The amount must have no more than 2 decimal places.",
            HttpStatus.BAD_REQUEST),
    USER_MODEL_INVALID_AMOUNT_NULL(
            "The amount must not be null.",
            HttpStatus.BAD_REQUEST),
    USER_MODEL_INSUFFICIENT_BALANCE(
        "The user does not have sufficient balance for this operation.",
        HttpStatus.BAD_REQUEST),

    //UserHandler related errors
    USER_HANDLER_USER_ALREADY_EXISTS(
            "A user with the same first and last name already exists.",
            HttpStatus.BAD_REQUEST)






    ;



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
