package com.example.casino.casino.response;

import java.util.List;

public interface ISlotmachineResponse {
    long getUser();

    double getAmount();

    boolean isWinning();

    List<String> getSlotStates();

    String getMessage();

}
