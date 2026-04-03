package com.example.casino.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SlotmachineResponseTest {

    private final Random random = new Random();

    @Test
    void getUser() {
        //setup
        long randomUser = random.nextLong();
        SlotmachineResponse response = new SlotmachineResponse(1L, randomUser, BigDecimal.ONE, true, "A,B,C", "Win");

        //test & assert
        assertEquals(randomUser, response.getUser());
    }

    @Test
    void getGameId() {
        //setup
        long randomGameId = random.nextLong();
        SlotmachineResponse response = new SlotmachineResponse(randomGameId, 1L, BigDecimal.ONE, true, "A,B,C", "Win");

        //test & assert
        assertEquals(randomGameId, response.getGameId());
    }

    @Test
    void getAmount() {
        //setup - extremely high value
        BigDecimal hugeAmount = new BigDecimal("999999999.99");
        SlotmachineResponse response = new SlotmachineResponse(1L, 1L, hugeAmount, true, "A,B,C", "Rich!");

        //test & assert
        assertEquals(hugeAmount, response.getAmount());
    }

    @Test
    void isWinning() {
        //test & assert for true
        SlotmachineResponse win = new SlotmachineResponse(1L, 1L, BigDecimal.TEN, true, "7,7,7", "Win");
        assertTrue(win.isWinning());

        //test & assert for false
        SlotmachineResponse loss = new SlotmachineResponse(1L, 1L, BigDecimal.ZERO, false, "X,Y,Z", "Loss");
        assertFalse(loss.isWinning());
    }

    @Test
    @DisplayName("Test getSlotStates - Boundary Case: Single Symbol")
    void getSlotStates_SingleSymbol() {
        //setup - only one symbol, no comma
        String singleState = "CHERRY";
        SlotmachineResponse response = new SlotmachineResponse(1L, 1L, BigDecimal.ZERO, false, singleState, "Loss");

        //test
        List<String> states = response.getSlotStates();

        //assert
        assertEquals(1, states.size());
        assertEquals("CHERRY", states.get(0));
    }

    @Test
    @DisplayName("Test getSlotStates - Standard Split")
    void getSlotStates_Split() {
        //setup
        String input = "LEMON,APPLE,CHERRY";
        SlotmachineResponse response = new SlotmachineResponse(1L, 1L, BigDecimal.ZERO, false, input, "Loss");

        //test
        List<String> states = response.getSlotStates();

        //assert
        assertEquals(3, states.size());
        assertEquals("APPLE", states.get(1));
    }

    @Test
    void getMessage() {
        //setup - very long message
        String longMessage = "A".repeat(1000);
        SlotmachineResponse response = new SlotmachineResponse(1L, 1L, BigDecimal.ZERO, false, "A,B,C", longMessage);

        //test & assert
        assertEquals(longMessage, response.getMessage());
    }

}