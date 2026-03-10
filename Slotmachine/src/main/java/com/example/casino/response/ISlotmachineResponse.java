package com.example.casino.response;

import java.math.BigDecimal;
import java.util.List;

public interface ISlotmachineResponse {

    long getUser();

    long getGameId();

    BigDecimal getAmount();

    boolean isWinning();

    List<String> getSlotStates();

    String getMessage();
}
