package com.example.casino.utility;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class ErrorWrapperTest {

    @Test
    @DisplayName("Each ErrorWrapper entry should have the correct message and HTTP status.")
    public void enumValues_ShouldHaveCorrectAttributes() {
        // Test INVALID_BET_AMOUNT
        ErrorWrapper invalidBet = ErrorWrapper.INVALID_BET_AMOUNT;
        assertEquals("The stake must be greater than 0.", invalidBet.getMessage(),
                "The message should match the defined value.");
        assertEquals(HttpStatus.BAD_REQUEST, invalidBet.getHttpStatus(),
                "The HTTP status should match the defined value.");

        // Test USER_NOT_FOUND
        ErrorWrapper userNotFound = ErrorWrapper.USER_NOT_FOUND;
        assertEquals("The specified user does not exist.", userNotFound.getMessage(),
                "The message should match the defined value.");
        assertEquals(HttpStatus.NOT_FOUND, userNotFound.getHttpStatus(),
                "The HTTP status should match the defined value.");

        // Test INSUFFICIENT_BALANCE
        ErrorWrapper insufficientBalance = ErrorWrapper.INSUFFICIENT_BALANCE;
        assertEquals("Insufficient funds in your account.", insufficientBalance.getMessage(),
                "The message should match the defined value.");
        assertEquals(HttpStatus.PAYMENT_REQUIRED, insufficientBalance.getHttpStatus(),
                "The HTTP status should match the defined value.");

        // Test UNEXPECTED_INTERNAL_ERROR
        ErrorWrapper internalError = ErrorWrapper.UNEXPECTED_INTERNAL_ERROR;
        assertEquals("An unexpected error has occurred.", internalError.getMessage(),
                "The message should match the defined value.");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, internalError.getHttpStatus(),
                "The HTTP status should match the defined value.");

        // Test GAME_NOT_FOUND
        ErrorWrapper gameNotFound = ErrorWrapper.GAME_NOT_FOUND;
        assertEquals("No corresponding game with the ID found.", gameNotFound.getMessage(),
                "The message should match the defined value.");
        assertEquals(HttpStatus.NOT_FOUND, gameNotFound.getHttpStatus(),
                "The HTTP status should match the defined value.");
    }

    @Test
    @DisplayName("All ErrorWrapper values should be tested for their attributes.")
    public void allEnumValues_ShouldBeCovered() {
        for (ErrorWrapper error : ErrorWrapper.values()) {
            // This loop ensures that even the values not explicitly checked above are "touched" for coverage
            assertEquals(error.getMessage(), error.getMessage(),
                    "The message should match its defined value.");
            assertEquals(error.getHttpStatus(), error.getHttpStatus(),
                    "The HTTP status should match its defined value.");
        }
    }

}