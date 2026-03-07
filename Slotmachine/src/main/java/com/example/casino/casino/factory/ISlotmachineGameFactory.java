package com.example.casino.casino.factory;

import com.example.casino.casino.model.ISlotmachineGameEntity;
import com.example.casino.casino.utility.ErrorWrapper;
import com.example.casino.casino.utility.Result;

public interface ISlotmachineGameFactory {

    Result<ISlotmachineGameEntity, ErrorWrapper> createSlotmachine(long userId, double betAmount, double winAmount, boolean isWinning, String slotResult);
}
