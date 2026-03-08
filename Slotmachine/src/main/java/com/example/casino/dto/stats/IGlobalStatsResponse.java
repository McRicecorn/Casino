package com.example.casino.dto.stats;

public interface IGlobalStatsResponse {
    long getTotalClientCount();
    long getTotalGamesCount();
    double getTotalProfit();
    double getTotalCashOut();
    double getTotalTurnover();

}
