package com.example.casino.casino.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to play a Slotmachine Game")
public class SlotmachineRequest implements ISlotmachineRequest{

    @Schema(description = "unique identifier of the user", example = "1")
    private final long user;

    @Schema(description = "the invested money", example = "100")
    private final double betAmount;

    @JsonCreator
    public SlotmachineRequest(@JsonProperty("user") long user,
                              @JsonProperty("betAmount") double betAmount){
        this.user = user;
        this.betAmount = betAmount;
    }

    @Override
    public long getUser() {
        return user;
    }

    @Override
    public double getBetAmount() {
        return betAmount;
    }
}
