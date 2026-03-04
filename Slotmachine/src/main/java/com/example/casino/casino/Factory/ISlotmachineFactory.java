package com.example.casino.casino.Factory;

import com.example.casino.casino.Model.ISlotmachineEntity;
import com.example.casino.casino.Utility.ErrorWrapper;
import com.example.casino.casino.Utility.Result;

public interface ISlotmachineFactory {

    Result<ISlotmachineEntity, ErrorWrapper> createSlotmachine(long userId, double betAmount, double winAmount, boolean isWinning, String slotResult);
}
