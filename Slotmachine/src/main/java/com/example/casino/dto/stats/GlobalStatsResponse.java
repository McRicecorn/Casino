package com.example.casino.dto.stats;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Response to view Stats for all games played")
public class GlobalStatsResponse implements IGlobalStatsResponse {
    @JsonProperty("total_client_count")
    private long totalClientCount;

    @JsonProperty("total_games_count")
    private long totalGamesCount;

    @JsonProperty("total_profit")
    private BigDecimal totalProfit;

    @JsonProperty("total_cash_out")
    private BigDecimal totalCashOut;

    @JsonProperty("total_turnover")
    private BigDecimal totalTurnover;

    public GlobalStatsResponse() {}

    public GlobalStatsResponse(long totalClientCount, long totalGamesCount, BigDecimal totalProfit, BigDecimal totalCashOut, BigDecimal totalTurnover ) {
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
    public BigDecimal getTotalProfit(){
        return totalProfit;
    }
    @Override
    public BigDecimal getTotalCashOut(){
        return totalCashOut;
    }
    @Override
    public BigDecimal getTotalTurnover(){
        return totalTurnover;
    }
}
