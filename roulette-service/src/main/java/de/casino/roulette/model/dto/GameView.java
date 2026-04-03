package de.casino.roulette.model.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class GameView {
  private long id;
  private long user;
  private BetType betType;
  private String betValue;
  private BigDecimal betAmount;
  private int ball_position;
  private boolean winning;
  private BigDecimal amount;
  private Instant createdAt;

  public GameView(long id, long user, BetType betType, String betValue, BigDecimal betAmount,
                  int ballPosition, boolean winning, BigDecimal amount, Instant createdAt) {
    this.id = id;
    this.user = user;
    this.betType = betType;
    this.betValue = betValue;
    this.betAmount = betAmount;
    this.ball_position = ballPosition;
    this.winning = winning;
    this.amount = amount;
    this.createdAt = createdAt;
  }

  public long getId() { return id; }
  public long getUser() { return user; }
  public BetType getBetType() { return betType; }
  public String getBetValue() { return betValue; }
  public BigDecimal getBetAmount() { return betAmount; }
  public int getBall_position() { return ball_position; }
  public boolean isWinning() { return winning; }
  public BigDecimal getAmount() { return amount; }
  public Instant getCreatedAt() { return createdAt; }
}
