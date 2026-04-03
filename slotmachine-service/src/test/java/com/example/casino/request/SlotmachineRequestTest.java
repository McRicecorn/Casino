package com.example.casino.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SlotmachineRequestTest {

    private long expectedUser = 1L;
    private BigDecimal expectedBetAmount = new BigDecimal("100.00");

    @Test
    void getUser() {
        //test
        SlotmachineRequest request = new SlotmachineRequest(expectedUser, expectedBetAmount);

        //assert
        assertEquals(expectedUser, request.getUser());
    }

    @Test
    void getBetAmount() {
        //test
        SlotmachineRequest request = new SlotmachineRequest(expectedUser, expectedBetAmount);

        //assert
        assertEquals(expectedBetAmount, request.getBetAmount());
    }

    @Test
    @DisplayName("Should handle edge cases like zero bet amount")
    void testSlotmachineRequestWithZeroBet() {
        //test
        BigDecimal zeroBet = BigDecimal.ZERO;
        SlotmachineRequest request = new SlotmachineRequest(expectedUser, zeroBet);

        //assert
        assertEquals(zeroBet, request.getBetAmount());
    }

    @Test
    @DisplayName("Test with extreme bet amounts and random user IDs")
    void testExtremeValues() {
        //random userID
        long randomUser = (long) (Math.random() * Long.MAX_VALUE);

        //extremely high bet
        BigDecimal hugeBet = new BigDecimal("999999999999.99");
        SlotmachineRequest requestHuge = new SlotmachineRequest(randomUser, hugeBet);
        assertEquals(hugeBet, requestHuge.getBetAmount());

        //tiny bet
        BigDecimal tinyBet = new BigDecimal("0.01");
        SlotmachineRequest requestTiny = new SlotmachineRequest(randomUser, tinyBet);
        assertEquals(tinyBet, requestTiny.getBetAmount());
    }
}