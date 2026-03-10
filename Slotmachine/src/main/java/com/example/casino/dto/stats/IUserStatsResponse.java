package com.example.casino.dto.stats;

import java.math.BigDecimal;

public interface IUserStatsResponse {
    long getClientId();
    int getTotalGamesCount();
    int getTotalWinnings();
    int getTotalLosses();
    BigDecimal getTotalClientProfit();
    BigDecimal getTotalTurnoverFromClient();
    BigDecimal getTotalProfitFromClient();
}
