package de.casino.banking_service.user.Utility;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorWrapperTest {

    @Test
    @DisplayName("Each ErrorWrapper entry should have the correct message and HTTP status.")
    public void enumValues_ShouldHaveCorrectAttributes() {

        // UNEXPECTED_INTERNAL_ERROR_OCCURED
        ErrorWrapper unexpected = ErrorWrapper.UNEXPECTED_INTERNAL_ERROR_OCCURED;
        assertEquals("An unexpected internal error occurred.", unexpected.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, unexpected.getHttpStatus());

        // USER_NOT_FOUND
        ErrorWrapper notFound = ErrorWrapper.USER_NOT_FOUND;
        assertEquals("A requested user could not be found.", notFound.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, notFound.getHttpStatus());

        // USER_MODEL_INVALID_NAME_Blank_OR_NULL
        ErrorWrapper invalidName = ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL;
        assertEquals("The first name and last name of a user must be non-empty.", invalidName.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, invalidName.getHttpStatus());

        // USER_MODEL_INVALID_NAME_LENGTH
        ErrorWrapper nameLength = ErrorWrapper.USER_MODEL_INVALID_NAME_LENGTH;
        assertEquals("The first name and last name of a user must not contain more than 12 characters.", nameLength.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, nameLength.getHttpStatus());

        // USER_MODEL_IDENTICAL_NAME
        ErrorWrapper identical = ErrorWrapper.USER_MODEL_IDENTICAL_NAME;
        assertEquals("This is already your first and last name.", identical.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, identical.getHttpStatus());

        // USER_MODEL_INVALID_AMOUNT
        ErrorWrapper invalidAmount = ErrorWrapper.USER_MODEL_INVALID_AMOUNT;
        assertEquals("The amount must be greater than 0.", invalidAmount.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, invalidAmount.getHttpStatus());

        // USER_MODEL_INVALID_AMOUNT_NEGATIVE
        ErrorWrapper negative = ErrorWrapper.USER_MODEL_INVALID_AMOUNT_NEGATIVE;
        assertEquals("The amount must be a positive number.", negative.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, negative.getHttpStatus());

        // USER_MODEL_INVALID_AMOUNT_DECIMAL_PLACES
        ErrorWrapper decimals = ErrorWrapper.USER_MODEL_INVALID_AMOUNT_DECIMAL_PLACES;
        assertEquals("The amount must have no more than 2 decimal places.", decimals.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, decimals.getHttpStatus());

        // USER_MODEL_INVALID_AMOUNT_NULL
        ErrorWrapper nullAmount = ErrorWrapper.USER_MODEL_INVALID_AMOUNT_NULL;
        assertEquals("The amount must not be null.", nullAmount.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, nullAmount.getHttpStatus());

        // USER_MODEL_INSUFFICIENT_BALANCE
        ErrorWrapper insufficient = ErrorWrapper.USER_MODEL_INSUFFICIENT_BALANCE;
        assertEquals("The user does not have sufficient balance for this operation.", insufficient.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, insufficient.getHttpStatus());

        // USER_HANDLER_USER_ALREADY_EXISTS
        ErrorWrapper exists = ErrorWrapper.USER_HANDLER_USER_ALREADY_EXISTS;
        assertEquals("A user with the same first and last name already exists.", exists.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exists.getHttpStatus());
    }

    @Test
    @DisplayName("All ErrorWrapper values should be covered.")
    public void allEnumValues_ShouldBeCovered() {
        for (ErrorWrapper error : ErrorWrapper.values()) {

            assertEquals(error.getMessage(), error.getMessage());
            assertEquals(error.getHttpStatus(), error.getHttpStatus());
        }
    }
}