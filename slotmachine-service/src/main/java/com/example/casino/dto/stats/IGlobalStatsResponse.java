package com.example.casino.dto.stats;

import java.math.BigDecimal;

public interface IGlobalStatsResponse {
    long getTotalClientCount();
    long getTotalGamesCount();
    BigDecimal getTotalProfit();
    BigDecimal getTotalCashOut();
    BigDecimal getTotalTurnover();

}
