package com.example.casino.casino.Response;

import org.hibernate.annotations.DialectOverride;

public class SlotmachineResponse implements ISlotmachineResponse{
    private long id;
    private double betAmount;
    private double winAmount;
    private boolean isWinning;
    private String slotResult;
    private double newBalance;
    private String message;

    public SlotmachineResponse(long id, double betAmount, double winAmount, boolean isWinning, String slotResult, double newBalance, String message) {
        this.id = id;
        this.betAmount = betAmount;
        this.winAmount = winAmount;
        this.isWinning = isWinning;
        this.slotResult = slotResult;
        this.newBalance = newBalance;
        this.message = message;
    }
    @Override
    public long getId() {
        return id;
    }
    @Override
    public double getBetAmount() {
        return betAmount;
    }

    @Override
    public double getWinAmount() {
        return winAmount;
    }

    @Override
    public boolean isWinning() {
        return isWinning;
    }

    @Override
    public String getSlotResult() {
        return slotResult;
    }

    @Override
    public double getNewBalance() {
        return newBalance;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
