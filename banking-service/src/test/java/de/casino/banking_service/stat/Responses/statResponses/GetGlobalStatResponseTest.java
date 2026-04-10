package de.casino.banking_service.stat.Responses.statResponses;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class GetGlobalStatResponseTest {

    @Test
    void constructor_shouldSetAllFieldsCorrectly() {

        GetGlobalStatResponse response = new GetGlobalStatResponse(
                new BigDecimal("150.00"),
                new BigDecimal("75.00"),
                12,
                new BigDecimal("75.00")
        );

        assertEquals(new BigDecimal("150.00"), response.getTotalGained());
        assertEquals(new BigDecimal("75.00"), response.getTotalLost());
        assertEquals(12, response.getTotalTransactions());
        assertEquals(new BigDecimal("75.00"), response.getNet());
    }

    @Test
    void getters_shouldReturnCorrectValues() {

        GetGlobalStatResponse response = new GetGlobalStatResponse(
                BigDecimal.TEN,
                BigDecimal.ONE,
                5,
                BigDecimal.valueOf(9)
        );

        assertEquals(BigDecimal.TEN, response.getTotalGained());
        assertEquals(BigDecimal.ONE, response.getTotalLost());
        assertEquals(5, response.getTotalTransactions());
        assertEquals(BigDecimal.valueOf(9), response.getNet());
    }
}