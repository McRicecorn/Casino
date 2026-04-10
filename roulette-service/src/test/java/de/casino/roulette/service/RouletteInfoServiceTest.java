package de.casino.roulette.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RouletteInfoServiceTest {

    private RouletteInfoService service;

    @BeforeEach
    void setUp() {
        service = new RouletteInfoService();
    }

    @Test
    void rules_returnsExpectedText() {
        String result = service.rules();

        assertNotNull(result);
        assertTrue(result.contains("European Roulette"));
        assertTrue(result.contains("NUMBER"));
        assertTrue(result.contains("COLOR"));
        assertTrue(result.contains("EVEN_ODD"));
        assertTrue(result.contains("HIGH_LOW"));
    }

    @Test
    void chances_returnsExpectedText() {
        String result = service.chances();

        assertNotNull(result);
        assertTrue(result.contains("37 slots"));
        assertTrue(result.contains("1/37"));
        assertTrue(result.contains("18/37"));
    }
}