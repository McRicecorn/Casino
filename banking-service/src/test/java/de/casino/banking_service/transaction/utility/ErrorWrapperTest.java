package de.casino.banking_service.transaction.utility;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class ErrorWrapperTest {

    @Test
    void enumValues_shouldExist() {
        for (ErrorWrapper error : ErrorWrapper.values()) {
            assertNotNull(error);
        }
    }

    @Test
    void getMessage_shouldReturnCorrectMessage() {
        assertEquals("The given amount is null",
                ErrorWrapper.AMOUNT_WAS_NULL.getMessage());

        assertEquals("The requested transaction was not found",
                ErrorWrapper.TRANSACTION_WAS_NOT_FOUND.getMessage());
    }

    @Test
    void getHttpStatus_shouldReturnCorrectStatus() {
        assertEquals(HttpStatus.BAD_REQUEST,
                ErrorWrapper.AMOUNT_WAS_NULL.getHttpStatus());

        assertEquals(HttpStatus.NOT_FOUND,
                ErrorWrapper.TRANSACTION_WAS_NOT_FOUND.getHttpStatus());
    }

    @Test
    void allErrors_shouldHaveMessageAndStatus() {
        for (ErrorWrapper error : ErrorWrapper.values()) {
            assertNotNull(error.getMessage());
            assertNotNull(error.getHttpStatus());
        }
    }
}