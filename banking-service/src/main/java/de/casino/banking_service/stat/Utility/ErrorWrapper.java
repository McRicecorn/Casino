package de.casino.banking_service.stat.Utility;

import org.springframework.http.HttpStatus;

public enum ErrorWrapper {

    // Create transaction errors
    AMOUNT_WAS_NULL(
            "The given amount is null",
            HttpStatus.BAD_REQUEST),

    AMOUNT_WAS_NEGATIVE(
            "The given amount is negative",
            HttpStatus.BAD_REQUEST),

    AMOUNT_HAS_TOO_MANY_DECIMAL_PLACES(
            "The given amount has more than 2 decimal places",
            HttpStatus.BAD_REQUEST),

    AMOUNT_WAS_ZERO(
            "The given amount is zero",
            HttpStatus.BAD_REQUEST),

    INVOICING_PARTY_DOES_NOT_EXIST(
            "The given invoicing party does not exist",
            HttpStatus.BAD_REQUEST),

    // transactionEntity errors
    TRANSACTION_WAS_NOT_FOUND(
            "The requested transaction was not found",
            HttpStatus.NOT_FOUND),

    USER_WAS_NOT_FOUND(
            "The given user could not be found",
            HttpStatus.NOT_FOUND),


    USER_TRANSACTION_FAILED(
            "Transaction on user failed (e.g. insufficient balance)",
            HttpStatus.BAD_REQUEST),

    // external service errors
    USER_SERVICE_UNAVAILABLE(
            "User service is not reachable",
            HttpStatus.SERVICE_UNAVAILABLE),

    USER_SERVICE_BAD_RESPONSE(
            "User service returned unexpected response",
            HttpStatus.BAD_GATEWAY),

    EXTERNAL_SERVICE_ERROR(
            "External service error occurred",
            HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorWrapper(String message, HttpStatus httpStatus){
        this.message = message;
        this.httpStatus =httpStatus;
    }
    public  String getMessage(){
        return message;
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }
}
