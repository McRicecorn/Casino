package com.example.casino.responseFactory;

import com.example.casino.model.ISlotmachineGameEntity;
import com.example.casino.response.ISlotmachineResponse;

public interface ISlotmachineResponseFactory {
    ISlotmachineResponse createSlotmachineResponse(ISlotmachineGameEntity slotmachine);
}
