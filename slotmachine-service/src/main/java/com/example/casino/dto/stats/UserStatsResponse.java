package com.example.casino.dto.stats;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Response to view stats for specific user/client")
public class UserStatsResponse implements IUserStatsResponse {
    @JsonProperty("client")
    private long client;

    @JsonProperty("total_games_count")
    private int totalGamesCount;

    @JsonProperty("total_winnings")
    private int totalWinnings;

    @JsonProperty("total_losses")
    private int totalLosses;

    @JsonProperty("total_client_profit")
    private BigDecimal totalClientProfit;

    @JsonProperty("total_house_turnover_from_client")
    private BigDecimal totalHouseTurnoverFromClient;

    @JsonProperty("total_house_profit_from_client")
    private BigDecimal totalHouseProfitFromClient;

    public UserStatsResponse() {}

    public UserStatsResponse(long client, int totalGamesCount, int totalWinnings, int totalLosses, BigDecimal totalClientProfit, BigDecimal totalHouseTurnoverFromClient, BigDecimal totalHouseProfitFromClient) {
        this.client = client;
        this.totalGamesCount = totalGamesCount;
        this.totalWinnings = totalWinnings;
        this.totalLosses = totalLosses;
        this.totalClientProfit = totalClientProfit;
        this.totalHouseProfitFromClient = totalHouseProfitFromClient;
        this.totalHouseTurnoverFromClient = totalHouseTurnoverFromClient;
    }

    @Override
    public long getClientId(){
        return client;
    }
    @Override
    public int getTotalGamesCount() {
        return totalGamesCount;
    }
    @Override
    public int getTotalWinnings() {
        return totalWinnings;
    }
    @Override
    public int getTotalLosses() {
        return totalLosses;
    }
    @Override
    public BigDecimal getTotalClientProfit() {
        return totalClientProfit;
    }
    @Override
    public BigDecimal getTotalTurnoverFromClient(){
        return totalHouseTurnoverFromClient;
    }
    @Override
    public BigDecimal getTotalHouseProfitFromClient(){
        return totalHouseProfitFromClient;
    }
}
