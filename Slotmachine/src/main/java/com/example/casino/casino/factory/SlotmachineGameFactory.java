package com.example.casino.casino.factory;

import com.example.casino.casino.model.ISlotmachineGameEntity;
import com.example.casino.casino.model.SlotmachineGameEntity;
import com.example.casino.casino.utility.ErrorWrapper;
import com.example.casino.casino.utility.Result;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SlotmachineGameFactory implements ISlotmachineGameFactory {

    @Override
    public Result<ISlotmachineGameEntity, ErrorWrapper> createSlotmachine(long userId, double betAmount, double winAmount, boolean isWinning, String slotResult){
        LocalDateTime timestamp = LocalDateTime.now();
        return SlotmachineGameEntity.create(userId, betAmount, winAmount, isWinning, slotResult, timestamp);
    }
}
