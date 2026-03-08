package com.example.casino.dto.stats;

public interface IUserStatsResponse {
    long getClientId();
    int getTotalGamesCount();
    int getTotalWinnings();
    int getTotalLosses();
    double getTotalClientProfit();
    double getTotalTurnoverFromClient();
    double getTotalProfitFromClient();
}
