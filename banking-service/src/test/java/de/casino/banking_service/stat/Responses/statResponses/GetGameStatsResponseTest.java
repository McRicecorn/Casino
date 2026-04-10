package de.casino.banking_service.stat.Responses.statResponses;

import de.casino.banking_service.common.Games;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class GetGameStatsResponseTest {

    @Test
    void constructor_shouldSetAllFieldsCorrectly() {

        int totalGamesPlayed = 12;
        int totalGamesWon = 7;
        int totalGamesLost = 5;
        BigDecimal totalGain = new BigDecimal("150");
        BigDecimal totalLoss = new BigDecimal("60");
        Games game = Games.ROULETTE;

        GetGameStatsResponse response = new GetGameStatsResponse(
                totalGamesPlayed,
                totalGamesWon,
                totalGamesLost,
                totalGain,
                totalLoss,
                game
        );

        assertEquals(totalGamesPlayed, response.getTotalGamesPlayed());
        assertEquals(totalGamesWon, response.getTotalGamesWon());
        assertEquals(totalGamesLost, response.getTotalGamesLost());
        assertEquals(totalGain, response.getTotalGain());
        assertEquals(totalLoss, response.getTotalLoss());
        assertEquals(game, response.getInvoicingParty());
    }
}