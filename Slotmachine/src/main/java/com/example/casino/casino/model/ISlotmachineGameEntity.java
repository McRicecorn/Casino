package com.example.casino.casino.model;

import com.example.casino.casino.utility.ErrorResult;
import com.example.casino.casino.utility.ErrorWrapper;

import java.time.LocalDateTime;

public interface ISlotmachineGameEntity {

    long getId();

    long getUserId();

    double getBetAmount();

    double getWinAmount();

    boolean isWinning();

    String getSlotResult();

    LocalDateTime getTimestamp();

    ErrorResult<ErrorWrapper> setBetAmount(double betAmount);
}
