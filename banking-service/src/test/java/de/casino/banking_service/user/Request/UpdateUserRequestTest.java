package de.casino.banking_service.user.Request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateUserRequestTest {

    private static Random rng;

    private static String minString;
    private static String maxString;

    @BeforeAll
    public static void setUpAll() {
        rng = new Random();

        minString = "A";
        maxString = "ABCDEFGHIJKL"; // 12 chars
    }

    @Test
    @DisplayName("Constructor initializes UpdateUserRequest correctly.")
    public void constructor_ShouldInitializeCorrectly() {
        String firstName = "Max";
        String lastName = "Mustermann";

        UpdateUserRequest request = new UpdateUserRequest(firstName, lastName);

        assertNotNull(request);
        assertEquals(firstName, request.firstName());
        assertEquals(lastName, request.lastName());
    }

    @Test
    @DisplayName("Getters return correct values for random input.")
    public void getters_ShouldReturnCorrectValues() {
        String randomFirst = "User" + rng.nextInt(1000);
        String randomLast = "Test" + rng.nextInt(1000);

        UpdateUserRequest request = new UpdateUserRequest(randomFirst, randomLast);

        assertEquals(randomFirst, request.firstName());
        assertEquals(randomLast, request.lastName());
    }

    @Test
    @DisplayName("Getters return correct values for edge cases.")
    public void getters_ShouldHandleEdgeCases() {
        UpdateUserRequest minRequest = new UpdateUserRequest(minString, minString);
        UpdateUserRequest maxRequest = new UpdateUserRequest(maxString, maxString);

        assertEquals(minString, minRequest.firstName());
        assertEquals(minString, minRequest.lastName());

        assertEquals(maxString, maxRequest.firstName());
        assertEquals(maxString, maxRequest.lastName());
    }
}