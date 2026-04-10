package de.casino.banking_service.stat.Responses.transactionResponses;

import de.casino.banking_service.common.Games;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class GetAllTransactionsTClientResponseTest {

    @Test
    void constructor_shouldSetAllFieldsCorrectly() {

        GetAllTransactionsTClientResponse response =
                new GetAllTransactionsTClientResponse(
                        1L,
                        99L,
                        new BigDecimal("10.50"),
                        Games.ROULETTE
                );

        assertEquals(1L, response.getId());
        assertEquals(99L, response.getUserId());
        assertEquals(new BigDecimal("10.50"), response.getAmount());
        assertEquals(Games.ROULETTE, response.getInvoicingParty());
    }

    @Test
    void getters_shouldReturnCorrectValues() {

        GetAllTransactionsTClientResponse response =
                new GetAllTransactionsTClientResponse(
                        5L,
                        42L,
                        BigDecimal.ZERO,
                        Games.SLOTS
                );

        assertEquals(5L, response.getId());
        assertEquals(42L, response.getUserId());
        assertEquals(BigDecimal.ZERO, response.getAmount());
        assertEquals(Games.SLOTS, response.getInvoicingParty());
    }
}