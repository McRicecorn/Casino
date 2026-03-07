package com.example.casino.casino.responseFactory;

import com.example.casino.casino.model.ISlotmachineGameEntity;
import com.example.casino.casino.response.ISlotmachineResponse;

public interface ISlotmachineResponseFactory {
    ISlotmachineResponse createSlotmachineResponse(ISlotmachineGameEntity slotmachine);
}
