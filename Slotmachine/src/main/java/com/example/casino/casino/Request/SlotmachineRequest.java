package com.example.casino.casino.Request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to play a Slotmachine Game")
public class SlotmachineRequest implements ISlotmachineRequest{

    @Schema(description = "unique identifier of the user", example = "1")
    private final long userId;
    @Schema(description = "the invested money", example = "100")
    private final double betAmount;

    @JsonCreator
    public SlotmachineRequest(@JsonProperty("UserId") long userId,
                              @JsonProperty("betAmount") double betAmount){
        this.userId = userId;
        this.betAmount = betAmount;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public double getBetAmount() {
        return betAmount;
    }
}
