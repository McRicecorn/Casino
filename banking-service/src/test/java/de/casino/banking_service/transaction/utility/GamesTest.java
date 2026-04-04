package de.casino.banking_service.transaction.utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GamesTest {

    @Test
    void enumValues_shouldExist() {
        assertNotNull(Games.ROULETTE);
        assertNotNull(Games.SLOTS);
    }

    @Test
    void getDisplayName_shouldReturnCorrectValue() {
        assertEquals("Roulette", Games.ROULETTE.getDisplayName());
        assertEquals("Slots", Games.SLOTS.getDisplayName());
    }

    @Test
    void valueOf_shouldReturnCorrectEnum() {
        assertEquals(Games.ROULETTE, Games.valueOf("ROULETTE"));
        assertEquals(Games.SLOTS, Games.valueOf("SLOTS"));
    }
}