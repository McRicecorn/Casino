package de.casino.banking_service.user.Request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class CreateUserRequestTest {

    private static Random rng;

    private static String minString;
    private static String maxString;

    @BeforeAll
    public static void setUpAll() {
        rng = new Random();

        minString = "A";
        maxString = "ABCDEFGHIJKL"; // 12 chars (dein Limit)
    }

    @Test
    @DisplayName("Constructor initializes CreateUserRequest correctly.")
    public void constructor_ShouldInitializeCorrectly() {
        String firstName = "John";
        String lastName = "Doe";

        CreateUserRequest request = new CreateUserRequest(firstName, lastName);

        assertNotNull(request);
        assertEquals(firstName, request.firstName());
        assertEquals(lastName, request.lastName());
    }

    @Test
    @DisplayName("Getters return correct values for random input.")
    public void getters_ShouldReturnCorrectValues() {
        String randomFirst = "User" + rng.nextInt(1000);
        String randomLast = "Test" + rng.nextInt(1000);

        CreateUserRequest request = new CreateUserRequest(randomFirst, randomLast);

        assertEquals(randomFirst, request.firstName());
        assertEquals(randomLast, request.lastName());
    }

    @Test
    @DisplayName("Getters return correct values for edge cases.")
    public void getters_ShouldHandleEdgeCases() {
        CreateUserRequest minRequest = new CreateUserRequest(minString, minString);
        CreateUserRequest maxRequest = new CreateUserRequest(maxString, maxString);

        assertEquals(minString, minRequest.firstName());
        assertEquals(minString, minRequest.lastName());

        assertEquals(maxString, maxRequest.firstName());
        assertEquals(maxString, maxRequest.lastName());
    }
}