package de.casino.roulette.model.dto;

import java.math.BigDecimal;

public class PlayResponse {
  private Long user;
  private boolean winning;
  private BigDecimal amount;
  private int ball_position;

  private BetType betType;
  private String betValue;
  private BigDecimal betAmount;

  public PlayResponse() {}

  public PlayResponse(Long user, boolean winning, BigDecimal amount, int ballPosition,
                      BetType betType, String betValue, BigDecimal betAmount) {
    this.user = user;
    this.winning = winning;
    this.amount = amount;
    this.ball_position = ballPosition;
    this.betType = betType;
    this.betValue = betValue;
    this.betAmount = betAmount;
  }

  public Long getUser() { return user; }
  public boolean isWinning() { return winning; }
  public BigDecimal getAmount() { return amount; }
  public int getBall_position() { return ball_position; }
  public BetType getBetType() { return betType; }
  public String getBetValue() { return betValue; }
  public BigDecimal getBetAmount() { return betAmount; }

  public void setUser(Long user) { this.user = user; }
  public void setWinning(boolean winning) { this.winning = winning; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }
  public void setBall_position(int ball_position) { this.ball_position = ball_position; }
  public void setBetType(BetType betType) { this.betType = betType; }
  public void setBetValue(String betValue) { this.betValue = betValue; }
  public void setBetAmount(BigDecimal betAmount) { this.betAmount = betAmount; }
}
