package de.casino.banking_service.stat.responseFactory;

import de.casino.banking_service.stat.Responses.statResponses.GetGameStatsResponse;
import de.casino.banking_service.stat.Responses.statResponses.GetGlobalStatResponse;
import de.casino.banking_service.common.Games;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class responseFactoryTest {

    private final responseFactory factory = new responseFactory();

    @Test
    void createGetGlobalStatResponse_shouldMapAllFieldsCorrectly() {

        BigDecimal totalGained = new BigDecimal("100");
        BigDecimal totalLost = new BigDecimal("40");
        int totalTransactions = 5;
        BigDecimal net = new BigDecimal("60");

        GetGlobalStatResponse response = factory.createGetGlobalStatResponse(
                totalGained,
                totalLost,
                totalTransactions,
                net
        );

        assertEquals(totalGained, response.getTotalGained());
        assertEquals(totalLost, response.getTotalLost());
        assertEquals(totalTransactions, response.getTotalTransactions());
        assertEquals(net, response.getNet());
    }

    @Test
    void createGetGameStatsResponse_shouldMapAllFieldsCorrectly() {

        int totalGamesPlayed = 10;
        int totalGamesWon = 6;
        int totalGamesLost = 4;
        BigDecimal totalGain = new BigDecimal("200");
        BigDecimal totalLoss = new BigDecimal("50");
        Games game = Games.ROULETTE;

        GetGameStatsResponse response = factory.createGetGameStatsResponse(
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