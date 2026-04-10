package de.casino.banking_service.stat.responseFactory;

import de.casino.banking_service.stat.Responses.statResponses.GetGameStatsResponse;
import de.casino.banking_service.stat.Responses.statResponses.GetGlobalStatResponse;
import de.casino.banking_service.common.Games;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class responseFactory {

    public GetGlobalStatResponse createGetGlobalStatResponse(
            BigDecimal totalGained,
            BigDecimal totalLost,
            int totalTransactions,
            BigDecimal net){
        return new GetGlobalStatResponse(
                totalGained,
                totalLost,
                totalTransactions,
                net);
    }

    public GetGameStatsResponse createGetGameStatsResponse(
            int totalGamesPlayed,
            int totalGamesWon,
            int totalGamesLost,
            BigDecimal totalGain,
            BigDecimal totalLoss,
            Games invoicingParty){
        return new GetGameStatsResponse(
                totalGamesPlayed,
                totalGamesWon,
                totalGamesLost,
                totalGain,
                totalLoss,
                invoicingParty);
    }

}
