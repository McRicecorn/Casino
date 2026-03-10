package com.example.casino.model;

import com.example.casino.utility.ErrorResult;
import com.example.casino.utility.ErrorWrapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ISlotmachineGameEntity {

    long getId();

    long getUserId();

    BigDecimal getBetAmount();

    BigDecimal getWinAmount();

    boolean isWinning();

    String getSlotResult();

    LocalDateTime getTimestamp();

    ErrorResult<ErrorWrapper> setBetAmount(BigDecimal betAmount);
}
