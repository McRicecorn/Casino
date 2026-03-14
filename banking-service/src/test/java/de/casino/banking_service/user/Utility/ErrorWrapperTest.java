package de.casino.banking_service.user.Utility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.casino.banking_service.user.Utility.ErrorWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ErrorWrapperTest {
/*
    @Test
    @DisplayName("Each ErrorWrapper entry should have the correct message and HTTP status.")
    public void enumValues_ShouldHaveCorrectAttributes() {
        // Test UNEXPECTED_INTERNAL_ERROR_OCCURED
        ErrorWrapper unexpectedError = ErrorWrapper.UNEXPECTED_INTERNAL_ERROR_OCCURED;
        assertEquals("An unexpected internal error occurred.", unexpectedError.getMessage(),
                "The message should match the defined value.");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, unexpectedError.getHttpStatus(),
                "The HTTP status should match the defined value.");

        // Test CIRCLE_REQUEST_INVALID_JSON_CANT_BE_MAPPED
        ErrorWrapper invalidJsonError = ErrorWrapper.CIRCLE_REQUEST_INVALID_JSON_CANT_BE_MAPPED;
        assertEquals("The request body could not be mapped to a circle.", invalidJsonError.getMessage(),
                "The message should match the defined value.");
        assertEquals(HttpStatus.BAD_REQUEST, invalidJsonError.getHttpStatus(),
                "The HTTP status should match the defined value.");

        // Test CIRCLE_NOT_FOUND
        ErrorWrapper circleNotFound = ErrorWrapper.CIRCLE_NOT_FOUND;
        assertEquals("A requested circle could not be found.", circleNotFound.getMessage(),
                "The message should match the defined value.");
        assertEquals(HttpStatus.NOT_FOUND, circleNotFound.getHttpStatus(),
                "The HTTP status should match the defined value.");

        // Test TRIANGLE_MODEL_INVALID_SIDE_INEQUALITY_BROKEN
        ErrorWrapper invalidTriangleInequality = ErrorWrapper.TRIANGLE_MODEL_INVALID_SIDE_INEQUALITY_BROKEN;
        assertEquals("The sides of a triangle do not satisfy the triangle inequality.",
                invalidTriangleInequality.getMessage(),
                "The message should match the defined value.");
        assertEquals(HttpStatus.BAD_REQUEST, invalidTriangleInequality.getHttpStatus(),
                "The HTTP status should match the defined value.");
    }

    @Test
    @DisplayName("All ErrorWrapper values should be tested for their attributes.")
    public void allEnumValues_ShouldBeCovered() {
        for (ErrorWrapper error : ErrorWrapper.values()) {
            assertEquals(error.getMessage(), error.getMessage(),
                    "The message should match its defined value.");
            assertEquals(error.getHttpStatus(), error.getHttpStatus(),
                    "The HTTP status should match its defined value.");
        }
    }

 */
}
