package com.example.casino.dto.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankingUserResponseTest {

    private BankingUserResponse response;
    private final long userId = 42L;
    private final String firstName = "uschi";
    private final String lastName = "meier";
    private final BigDecimal balance = new BigDecimal("1000.99");

    @BeforeEach
    void setUp() {
        response = new BankingUserResponse(userId, firstName, lastName, balance);
    }

    @Test
    void getUserId() {
        assertEquals(userId, response.getUserId());
    }

    @Test
    void getFirstName() {
        assertEquals(firstName, response.getFirstName());
    }

    @Test
    void getLastName() {
        assertEquals(lastName, response.getLastName());
    }

    @Test
    void getBalance() {
        assertEquals(balance, response.getBalance());
    }
}