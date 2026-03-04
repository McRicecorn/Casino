package com.example.casino.casino.ResponseFactory;

import com.example.casino.casino.Model.ISlotmachineEntity;
import com.example.casino.casino.Response.ISlotmachineResponse;

public interface ISlotmachineResponseFactory {
    ISlotmachineResponse createSlotmachineResponse(ISlotmachineEntity slotmachine);
}
