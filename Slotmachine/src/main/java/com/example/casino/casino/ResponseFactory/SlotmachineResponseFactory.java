package com.example.casino.casino.ResponseFactory;

import com.example.casino.casino.Factory.SlotmachineFactory;
import com.example.casino.casino.Model.ISlotmachineEntity;
import com.example.casino.casino.Response.ISlotmachineResponse;
import com.example.casino.casino.Response.SlotmachineResponse;
import org.springframework.stereotype.Component;

@Component
public class SlotmachineResponseFactory implements ISlotmachineResponseFactory {
    @Override
    public ISlotmachineResponse createSlotmachineResponse(ISlotmachineEntity slotmachine) {

        String message = slotmachine.isWinning() ? "Gewonnen!" : "Leider verloren.";
        return new SlotmachineResponse(
                slotmachine.getId(),
                slotmachine.getBetAmount(),
                slotmachine.getWinAmount(),
                slotmachine.isWinning(),
                slotmachine.getSlotResult(),
                0.0,
                message);
    }
}
