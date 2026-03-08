package com.example.casino.casino.response;

import java.util.List;

public interface ISlotmachineResponse {

    long getUser();

    long getGameId();

    double getAmount();

    boolean isWinning();

    List<String> getSlotStates();

    String getMessage();
}
