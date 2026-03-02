package com.example.casino.casino.Model;

import com.example.casino.casino.Utility.ErrorResult;
import com.example.casino.casino.Utility.ErrorWrapper;

import java.time.LocalDateTime;

public interface ISlotmachineEntity {

    long getId();

    long getUserId();

    double getBetAmount();

    double getWinAmount();

    boolean isWinning();

    String getSlotResult();

    LocalDateTime getTimestamp();

    ErrorResult<ErrorWrapper> setBetAmount(double betAmount);
}
