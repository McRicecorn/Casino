package de.casino.banking_service.transaction.utility;

import static org.junit.jupiter.api.Assertions.*;

import de.casino.banking_service.user.Utility.ErrorResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class ErrorResultTest {

    @Test
    @DisplayName("Success factory method creates a successful ErrorResult.")
    public void success_ShouldCreateSuccessfulErrorResult() {
        // Act
        ErrorResult<String> result = ErrorResult.success();

        // Assert
        assertNotNull(result, "The ErrorResult object should not be null.");
        assertTrue(result.isSuccess(), "The ErrorResult should represent a success.");
        assertFalse(result.isFailure(), "The ErrorResult should not represent a failure.");
        assertEquals(Optional.empty(), result.getFailureData(), "The failure data should be empty for a success.");
    }

    @Test
    @DisplayName("Failure factory method creates a failed ErrorResult.")
    public void failure_ShouldCreateFailedErrorResult() {
        // Arrange
        String failureData = "Error occurred!";

        // Act
        ErrorResult<String> result = ErrorResult.failure(failureData);

        // Assert
        assertNotNull(result, "The ErrorResult object should not be null.");
        assertTrue(result.isFailure(), "The ErrorResult should represent a failure.");
        assertFalse(result.isSuccess(), "The ErrorResult should not represent a success.");
        assertEquals(Optional.of(failureData), result.getFailureData(), "The failure data should match the initialized value.");
    }

    @Test
    @DisplayName("isSuccess and isFailure return correct values.")
    public void isSuccessAndIsFailure_ShouldReturnCorrectValues() {
        // Success case
        ErrorResult<String> successResult = ErrorResult.success();
        assertTrue(successResult.isSuccess(), "The ErrorResult should represent a success.");
        assertFalse(successResult.isFailure(), "The ErrorResult should not represent a failure.");

        // Failure case
        ErrorResult<String> failureResult = ErrorResult.failure("Error occurred!");
        assertTrue(failureResult.isFailure(), "The ErrorResult should represent a failure.");
        assertFalse(failureResult.isSuccess(), "The ErrorResult should not represent a success.");
    }

    @Test
    @DisplayName("getFailureData returns correct data or empty Optional.")
    public void getFailureData_ShouldReturnCorrectValues() {
        // Success case
        ErrorResult<String> successResult = ErrorResult.success();
        assertEquals(Optional.empty(), successResult.getFailureData(), "The failure data should be empty for a success.");

        // Failure case
        String failureData = "Error occurred!";
        ErrorResult<String> failureResult = ErrorResult.failure(failureData);
        assertEquals(Optional.of(failureData), failureResult.getFailureData(), "The failure data should match the expected value.");
    }

    @Test
    @DisplayName("Optional handling of failure data works correctly.")
    public void optionalFailureDataHandling_ShouldWorkCorrectly() {
        // Failure case
        ErrorResult<String> failureResult = ErrorResult.failure("Error occurred!");
        failureResult.getFailureData().ifPresentOrElse(
                data -> assertEquals("Error occurred!", data, "The failure data should match the expected value."),
                () -> fail("The failure data should be present.")
        );

        // Success case
        ErrorResult<String> successResult = ErrorResult.success();
        successResult.getFailureData().ifPresent(
                data -> fail("The failure data should not be present for a success.")
        );
    }
}
