package de.casino.banking_service.user.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteUserResponseTest {

    private static Random rng;

    private String randomFirstName;
    private String randomLastName;
    private BigDecimal randomBalance;

    private DeleteUserResponse response;

    @BeforeAll
    static void setUpAll() {
        rng = new Random();
    }

    @BeforeEach
    void setUp() {
        randomFirstName = "First" + rng.nextInt(1000);
        randomLastName = "Last" + rng.nextInt(1000);
        randomBalance = BigDecimal.valueOf(rng.nextDouble() * 1000)
                .setScale(2, RoundingMode.HALF_UP);

        response = new DeleteUserResponse(
                randomFirstName,
                randomLastName,
                randomBalance
        );
    }

    @Test
    @DisplayName("firstName() returns correct value")
    void firstName_ShouldReturnCorrectValue() {
        assertEquals(randomFirstName, response.firstName());
    }

    @Test
    @DisplayName("lastName() returns correct value")
    void lastName_ShouldReturnCorrectValue() {
        assertEquals(randomLastName, response.lastName());
    }

    @Test
    @DisplayName("balance() returns correct value")
    void balance_ShouldReturnCorrectValue() {
        assertEquals(randomBalance, response.balance());
    }

    @Test
    @DisplayName("constructor works with edge cases")
    void constructor_WithEdgeCases_ShouldWork() {

        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal max = new BigDecimal("999999999999.99");

        DeleteUserResponse r1 = new DeleteUserResponse("A", "B", zero);
        DeleteUserResponse r2 = new DeleteUserResponse("X", "Y", max);

        assertEquals(zero, r1.balance());
        assertEquals(max, r2.balance());
    }
}