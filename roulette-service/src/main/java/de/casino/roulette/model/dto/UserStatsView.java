package de.casino.roulette.model.dto;

import java.math.BigDecimal;

public class UserStatsView {
  private long user;
  private long games_count;
  private long wins;
  private long losses;
  private BigDecimal turnover;
  private BigDecimal cash_out;
  private BigDecimal profit;

  public UserStatsView(long user, long gamesCount, long wins, long losses,
                       BigDecimal turnover, BigDecimal cashOut, BigDecimal profit) {
    this.user = user;
    this.games_count = gamesCount;
    this.wins = wins;
    this.losses = losses;
    this.turnover = turnover;
    this.cash_out = cashOut;
    this.profit = profit;
  }

  public long getUser() { return user; }
  public long getGames_count() { return games_count; }
  public long getWins() { return wins; }
  public long getLosses() { return losses; }
  public BigDecimal getTurnover() { return turnover; }
  public BigDecimal getCash_out() { return cash_out; }
  public BigDecimal getProfit() { return profit; }
}
