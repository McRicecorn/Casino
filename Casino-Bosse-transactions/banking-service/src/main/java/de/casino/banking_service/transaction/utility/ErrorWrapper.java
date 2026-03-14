package de.casino.banking_service.transaction.utility;

import org.springframework.http.HttpStatus;

public enum ErrorWrapper {
    AMOUNT_WAS_NEGATIVE_OR_NULL("Send payment was less than zero or zero.",
          HttpStatus.BAD_REQUEST),
    TRANSACTION_WAS_NOT_FOUND("The requested Transaction was not found",
            HttpStatus.NOT_FOUND),
    INVOICING_PARTY_DOES_NOT_EXIST("The given Invoicing Party does not exist",HttpStatus.NOT_FOUND);

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
