package com.example.casino.casino.Response;

public interface ISlotmachineResponse {
    long getId();

    double getBetAmount();

    double getWinAmount();

    boolean isWinning();

    String getSlotResult();

    String getMessage();

    double getNewBalance();

}
