package de.casino.roulette.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class PlayRequest {

  @NotNull
  private Long user;

  @NotNull
  @DecimalMin(value = "0.01")
  private BigDecimal betAmount;

  @NotNull
  private BetType betType;

  @NotBlank
  private String betValue;

  public Long getUser() { return user; }
  public void setUser(Long user) { this.user = user; }

  public BigDecimal getBetAmount() { return betAmount; }
  public void setBetAmount(BigDecimal betAmount) { this.betAmount = betAmount; }

  public BetType getBetType() { return betType; }
  public void setBetType(BetType betType) { this.betType = betType; }

  public String getBetValue() { return betValue; }
  public void setBetValue(String betValue) { this.betValue = betValue; }
}
