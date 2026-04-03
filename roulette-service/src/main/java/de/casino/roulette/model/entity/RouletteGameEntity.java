package de.casino.roulette.model.entity;

import de.casino.roulette.model.dto.BetType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "roulette_game")
public class RouletteGameEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private BetType betType;

  @Column(nullable = false)
  private String betValue;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal betAmount;

  @Column(nullable = false)
  private int ballPosition;

  @Column(nullable = false)
  private boolean winning;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal amount;

  @Column(nullable = false)
  private Instant createdAt;

  protected RouletteGameEntity() {}

  public RouletteGameEntity(Long userId, BetType betType, String betValue, BigDecimal betAmount,
                            int ballPosition, boolean winning, BigDecimal amount, Instant createdAt) {
    this.userId = userId;
    this.betType = betType;
    this.betValue = betValue;
    this.betAmount = betAmount;
    this.ballPosition = ballPosition;
    this.winning = winning;
    this.amount = amount;
    this.createdAt = createdAt;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Long getUserId() { return userId; }
  public BetType getBetType() { return betType; }
  public String getBetValue() { return betValue; }
  public BigDecimal getBetAmount() { return betAmount; }
  public int getBallPosition() { return ballPosition; }
  public boolean isWinning() { return winning; }
  public BigDecimal getAmount() { return amount; }
  public Instant getCreatedAt() { return createdAt; }
}
