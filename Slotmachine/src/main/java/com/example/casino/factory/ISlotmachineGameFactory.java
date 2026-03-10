package com.example.casino.factory;

import com.example.casino.model.ISlotmachineGameEntity;
import com.example.casino.utility.ErrorWrapper;
import com.example.casino.utility.Result;

import java.math.BigDecimal;

public interface ISlotmachineGameFactory {

    Result<ISlotmachineGameEntity, ErrorWrapper> createSlotmachine(long userId, BigDecimal betAmount, BigDecimal winAmount, boolean isWinning, String slotResult);
}
