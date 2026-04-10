package de.casino.banking_service.stat.Responses.statResponses;

import de.casino.banking_service.common.Games;

import java.math.BigDecimal;

public class GetGameStatsResponse {

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public int getTotalGamesWon() {
        return totalGamesWon;
    }

    public int getTotalGamesLost() {
        return totalGamesLost;
    }

    public BigDecimal getTotalGain() {
        return totalGain;
    }

    public Games getInvoicingParty() {
        return invoicingParty;
    }

    public BigDecimal getTotalLoss() {
        return totalLoss;
    }

    private int totalGamesPlayed;
    private int totalGamesWon;
    private int totalGamesLost;
    private BigDecimal totalGain;
    private BigDecimal totalLoss;
    private Games invoicingParty;

    public GetGameStatsResponse(int totalGamesPlayed, int totalGamesWon, int totalGamesLost, BigDecimal totalGain, BigDecimal totalLoss, Games invoicingParty) {
        this.totalGamesPlayed = totalGamesPlayed;
        this.totalGamesWon = totalGamesWon;
        this.totalGamesLost = totalGamesLost;
        this.totalGain = totalGain;
        this.totalLoss = totalLoss;
        this.invoicingParty = invoicingParty;
    }


}
