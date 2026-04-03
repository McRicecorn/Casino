package com.example.casino.dto.stats;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class GlobalStatsResponseTest {

    private long expectedClientCount = 100L;
    private long expectedGamesCount = 500L;
    private BigDecimal expectedProfit = new BigDecimal("1500.00");
    private BigDecimal expectedCashOut = new BigDecimal("3500.00");
    private BigDecimal expectedTurnover = new BigDecimal("5000.00");

    @Test
    void getTotalClientCount() {
        GlobalStatsResponse response = new GlobalStatsResponse(expectedClientCount, expectedGamesCount, expectedProfit, expectedCashOut, expectedTurnover);

        //assert
        assertEquals(expectedClientCount, response.getTotalClientCount());
    }

    @Test
    void getTotalGamesCount() {
        GlobalStatsResponse response = new GlobalStatsResponse(expectedClientCount, expectedGamesCount, expectedProfit, expectedCashOut, expectedTurnover);

        //assert
        assertEquals(expectedGamesCount, response.getTotalGamesCount());
    }

    @Test
    void getTotalProfit() {
        GlobalStatsResponse response = new GlobalStatsResponse(expectedClientCount, expectedGamesCount, expectedProfit, expectedCashOut, expectedTurnover);

        //assert
        assertEquals(expectedProfit, response.getTotalProfit());
    }

    @Test
    void getTotalCashOut() {
        GlobalStatsResponse response = new GlobalStatsResponse(expectedClientCount, expectedGamesCount, expectedProfit, expectedCashOut, expectedTurnover);

        //assert
        assertEquals(expectedCashOut, response.getTotalCashOut());
    }

    @Test
    void getTotalTurnover() {
        GlobalStatsResponse response = new GlobalStatsResponse(expectedClientCount, expectedGamesCount, expectedProfit, expectedCashOut, expectedTurnover);

        //assert
        assertEquals(expectedTurnover, response.getTotalTurnover());
    }

    @Test
    @DisplayName("Test default constructor")
    void testDefaultConstructor() {
        //test
        GlobalStatsResponse response = new GlobalStatsResponse();

        //assert
        assertNotNull(response);
        assertEquals(0L, response.getTotalClientCount());
        assertNull(response.getTotalProfit());
    }
}