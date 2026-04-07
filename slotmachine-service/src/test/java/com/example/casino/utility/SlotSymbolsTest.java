package com.example.casino.utility;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlotSymbolsTest {

    @Test
    @DisplayName("Verify enum values names")
    void slotSymbols_NamesShouldMatch() {
        for (SlotSymbols symbol : SlotSymbols.values()) {
            assertNotNull(symbol.name(), "The name of the symbol should not be null.");
        }
    }

    @Test
    @DisplayName("Verify that valueOf throws exception for unknown symbols")
    void valueOf_ShouldThrowExceptionOnUnknown() {
        assertThrows(IllegalArgumentException.class, () -> {
            SlotSymbols.valueOf("NON_EXISTENT_FRUIT");
        });
    }
}