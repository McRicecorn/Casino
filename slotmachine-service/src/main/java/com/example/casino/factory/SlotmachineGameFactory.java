package com.example.casino.factory;

import com.example.casino.model.ISlotmachineGameEntity;
import com.example.casino.model.SlotmachineGameEntity;
import com.example.casino.utility.ErrorWrapper;
import com.example.casino.utility.Result;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class SlotmachineGameFactory implements ISlotmachineGameFactory {

    @Override
    public Result<ISlotmachineGameEntity, ErrorWrapper> createSlotmachine(long userId, BigDecimal betAmount, BigDecimal winAmount, boolean isWinning, String slotResult){
        LocalDateTime timestamp = LocalDateTime.now();
        return SlotmachineGameEntity.create(userId, betAmount, winAmount, isWinning, slotResult, timestamp);
    }
}
