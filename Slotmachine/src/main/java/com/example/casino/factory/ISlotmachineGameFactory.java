package com.example.casino.factory;

import com.example.casino.model.ISlotmachineGameEntity;
import com.example.casino.utility.ErrorWrapper;
import com.example.casino.utility.Result;

public interface ISlotmachineGameFactory {

    Result<ISlotmachineGameEntity, ErrorWrapper> createSlotmachine(long userId, double betAmount, double winAmount, boolean isWinning, String slotResult);
}
