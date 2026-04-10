package de.casino.banking_service.stat.Responses.statResponses;

import java.math.BigDecimal;

public class GetGlobalStatResponse {

    public GetGlobalStatResponse(BigDecimal totalGained, BigDecimal totalLost, int totalTransactions, BigDecimal net){
        this.totalGained = totalGained;
        this.totalLost = totalLost;
        this.totalTransactions = totalTransactions;
        this.net = net;
    }

    public BigDecimal getTotalGained() {
        return totalGained;
    }

    public BigDecimal getTotalLost() {
        return totalLost;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public BigDecimal getNet() {
        return net;
    }

    private BigDecimal totalGained;
    private BigDecimal totalLost;
    private int totalTransactions;
    private BigDecimal net;
}
