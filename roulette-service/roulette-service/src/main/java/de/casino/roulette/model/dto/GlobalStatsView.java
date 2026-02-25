package de.casino.roulette.model.dto;

import java.math.BigDecimal;

public class GlobalStatsView {
  private long games_count;
  private long client_count;
  private BigDecimal total_turnover;
  private BigDecimal total_cash_out;
  private BigDecimal total_profit;

  public GlobalStatsView(long gamesCount, long clientCount,
                         BigDecimal turnover, BigDecimal cashOut, BigDecimal profit) {
    this.games_count = gamesCount;
    this.client_count = clientCount;
    this.total_turnover = turnover;
    this.total_cash_out = cashOut;
    this.total_profit = profit;
  }

  public long getGames_count() { return games_count; }
  public long getClient_count() { return client_count; }
  public BigDecimal getTotal_turnover() { return total_turnover; }
  public BigDecimal getTotal_cash_out() { return total_cash_out; }
  public BigDecimal getTotal_profit() { return total_profit; }
}
