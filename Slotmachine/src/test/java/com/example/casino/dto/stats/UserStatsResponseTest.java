package com.example.casino.dto.stats;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserStatsResponseTest {

    private long expectedClient = 1L;
    private int expectedGamesCount = 10;
    private int expectedWinnings = 3;
    private int expectedLosses = 7;
    private BigDecimal expectedClientProfit = new BigDecimal("-50.00");
    private BigDecimal expectedHouseTurnover = new BigDecimal("100.00");
    private BigDecimal expectedHouseProfit = new BigDecimal("50.00");

    @Test
    void getClientId() {
        UserStatsResponse response = new UserStatsResponse(expectedClient, expectedGamesCount, expectedWinnings, expectedLosses, expectedClientProfit, expectedHouseTurnover, expectedHouseProfit);

        //assert
        assertEquals(expectedClient, response.getClientId());
    }

    @Test
    void getTotalGamesCount() {
        UserStatsResponse response = new UserStatsResponse(expectedClient, expectedGamesCount, expectedWinnings, expectedLosses, expectedClientProfit, expectedHouseTurnover, expectedHouseProfit);

        //assert
        assertEquals(expectedGamesCount, response.getTotalGamesCount());
    }

    @Test
    void getTotalWinnings() {
        UserStatsResponse response = new UserStatsResponse(expectedClient, expectedGamesCount, expectedWinnings, expectedLosses, expectedClientProfit, expectedHouseTurnover, expectedHouseProfit);

        //assert
        assertEquals(expectedWinnings, response.getTotalWinnings());
    }

    @Test
    void getTotalLosses() {
        UserStatsResponse response = new UserStatsResponse(expectedClient, expectedGamesCount, expectedWinnings, expectedLosses, expectedClientProfit, expectedHouseTurnover, expectedHouseProfit);

        //assert
        assertEquals(expectedLosses, response.getTotalLosses());
    }

    @Test
    void getTotalClientProfit() {
        UserStatsResponse response = new UserStatsResponse(expectedClient, expectedGamesCount, expectedWinnings, expectedLosses, expectedClientProfit, expectedHouseTurnover, expectedHouseProfit);

        //assert
        assertEquals(expectedClientProfit, response.getTotalClientProfit());
    }

    @Test
    void getTotalTurnoverFromClient() {
        UserStatsResponse response = new UserStatsResponse(expectedClient, expectedGamesCount, expectedWinnings, expectedLosses, expectedClientProfit, expectedHouseTurnover, expectedHouseProfit);

        //assert
        assertEquals(expectedHouseTurnover, response.getTotalTurnoverFromClient());
    }

    @Test
    void getTotalHouseProfitFromClient() {
        UserStatsResponse response = new UserStatsResponse(expectedClient, expectedGamesCount, expectedWinnings, expectedLosses, expectedClientProfit, expectedHouseTurnover, expectedHouseProfit);

        //assert
        assertEquals(expectedHouseProfit, response.getTotalHouseProfitFromClient());
    }

    @Test
    @DisplayName("Test default constructor")
    void testDefaultConstructor() {
        //test
        UserStatsResponse response = new UserStatsResponse();

        //assert
        assertNotNull(response);
        assertEquals(0L, response.getClientId());
        assertNull(response.getTotalClientProfit());
    }
}