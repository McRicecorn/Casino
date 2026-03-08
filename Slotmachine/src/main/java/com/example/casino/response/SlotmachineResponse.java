package com.example.casino.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;
import java.util.List;

@Schema(description = "Response containing Game Details")
public class SlotmachineResponse implements ISlotmachineResponse{

    @Schema(description = "unique identifier of the game", example = "1")
    private long gameId;

    @Schema(description = "unique identifier of the player", example = "1")
    private long user;

    @Schema(description = "Amount/Money earned/lost", example = "6")
    private double amount;

    @Schema(description = "Boolean, whether player is winning or not", example = "true")
    private boolean winning;

    @Schema(description = "Symbols shown on the Slot Reels (visual result)", example = "CHERRY,CHERRY,LEMON")
    @JsonProperty("slot_states")
    private List<String> slotStates;

    @Schema(description = "readable and easy to understand feedback for the player about the outcome of the game", example = "FANTASTIC! You WON!")
    private String message;


    public SlotmachineResponse(long gameId, long user, double amount, boolean winning, String slotStates, String message) {
        this.gameId = gameId;
        this.user = user;
        this.amount = amount;
        this.winning = winning;
        this.slotStates = Arrays.asList(slotStates.split(","));
        this.message = message;
    }

    @Override
    public long getUser(){
        return user;
    }

    @Override
    public long getGameId() {
        return gameId;
    }
    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public boolean isWinning() {
        return winning;
    }

    @Override
    public List<String> getSlotStates() {
        return slotStates;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
