package com.example.casino.utility;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    @DisplayName("Success factory method creates a successful Result.")
    public void success_ShouldCreateSuccessfulResult() {
        // Arrange
        String successData = "Success!";

        // Act
        Result<String, String> result = Result.success(successData);

        // Assert
        assertNotNull(result, "The Result object should not be null.");
        assertTrue(result.isSuccess(), "The Result should represent a success.");
        assertFalse(result.isFailure(), "The Result should not represent a failure.");
        assertEquals(Optional.of(successData), result.getSuccessData(), "The success data should match the initialized value.");
        assertEquals(Optional.empty(), result.getFailureData(), "The failure data should be empty.");
    }

    @Test
    @DisplayName("Failure factory method creates a failed Result.")
    public void failure_ShouldCreateFailedResult() {
        // Arrange
        String failureData = "Error occurred!";

        // Act
        Result<String, String> result = Result.failure(failureData);

        // Assert
        assertNotNull(result, "The Result object should not be null.");
        assertTrue(result.isFailure(), "The Result should represent a failure.");
        assertFalse(result.isSuccess(), "The Result should not represent a success.");
        assertEquals(Optional.of(failureData), result.getFailureData(), "The failure data should match the initialized value.");
        assertEquals(Optional.empty(), result.getSuccessData(), "The success data should be empty.");
    }

    @Test
    @DisplayName("isSuccess and isFailure return correct values.")
    public void isSuccessAndIsFailure_ShouldReturnCorrectValues() {
        // Success case
        Result<String, String> successResult = Result.success("Success!");
        assertTrue(successResult.isSuccess(), "The Result should represent a success.");
        assertFalse(successResult.isFailure(), "The Result should not represent a failure.");

        // Failure case
        Result<String, String> failureResult = Result.failure("Error occurred!");
        assertTrue(failureResult.isFailure(), "The Result should represent a failure.");
        assertFalse(failureResult.isSuccess(), "The Result should not represent a success.");
    }

    @Test
    @DisplayName("getSuccessData and getFailureData return correct values.")
    public void getters_ShouldReturnCorrectValues() {
        // Success case
        String successData = "Success!";
        Result<String, String> successResult = Result.success(successData);
        assertEquals(Optional.of(successData), successResult.getSuccessData(), "The success data should match.");
        assertEquals(Optional.empty(), successResult.getFailureData(), "The failure data should be empty for a success.");

        // Failure case
        String failureData = "Error occurred!";
        Result<String, String> failureResult = Result.failure(failureData);
        assertEquals(Optional.of(failureData), failureResult.getFailureData(), "The failure data should match.");
        assertEquals(Optional.empty(), failureResult.getSuccessData(), "The success data should be empty for a failure.");
    }

    @Test
    @DisplayName("Optional handling of success and failure data works correctly.")
    public void optionalDataHandling_ShouldWorkCorrectly() {
        // Success case
        Result<String, String> successResult = Result.success("Success!");
        successResult.getSuccessData().ifPresentOrElse(
                data -> assertEquals("Success!", data, "The success data should match the expected value."),
                () -> fail("The success data should be present.")
        );

        // Failure case
        Result<String, String> failureResult = Result.failure("Error occurred!");
        failureResult.getFailureData().ifPresentOrElse(
                data -> assertEquals("Error occurred!", data, "The failure data should match the expected value."),
                () -> fail("The failure data should be present.")
        );
    }

}