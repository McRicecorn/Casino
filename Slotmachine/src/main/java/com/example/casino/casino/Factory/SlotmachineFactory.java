package com.example.casino.casino.Factory;

import com.example.casino.casino.Model.ISlotmachineEntity;
import com.example.casino.casino.Model.SlotmachineEntity;
import com.example.casino.casino.Utility.ErrorWrapper;
import com.example.casino.casino.Utility.Result;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SlotmachineFactory implements ISlotmachineFactory {

    @Override
    public Result<ISlotmachineEntity, ErrorWrapper> createSlotmachine(long userId, double betAmount, double winAmount, boolean isWinning, String slotResult){
        LocalDateTime timestamp = LocalDateTime.now();
        return SlotmachineEntity.create(userId, betAmount, winAmount, isWinning, slotResult, timestamp);
    }
}
