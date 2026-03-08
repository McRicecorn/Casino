package com.example.casino.dto.stats;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response to view Stats for all games played")
public class GlobalStatsResponse implements IGlobalStatsResponse {
    @JsonProperty("total_client_count")
    private long totalClientCount;

    @JsonProperty("total_games_count")
    private long totalGamesCount;

    @JsonProperty("total_profit")
    private double totalProfit;

    @JsonProperty("total_cash_out")
    private double totalCashOut;

    @JsonProperty("total_turnover")
    private double totalTurnover;

    public GlobalStatsResponse() {}

    public GlobalStatsResponse(long totalClientCount, long totalGamesCount, double totalProfit, double totalCashOut, double totalTurnover ) {
        this.totalClientCount = totalClientCount;
        this.totalGamesCount = totalGamesCount;
        this.totalProfit = totalProfit;
        this.totalCashOut = totalCashOut;
        this.totalTurnover = totalTurnover;
    }
    @Override
    public long getTotalClientCount() {
        return totalClientCount;
    }
    @Override
    public long getTotalGamesCount() {
        return totalGamesCount;
    }
    @Override
    public double getTotalProfit(){
        return totalProfit;
    }
    @Override
    public double getTotalCashOut(){
        return totalCashOut;
    }
    @Override
    public double getTotalTurnover(){
        return totalTurnover;
    }
}
