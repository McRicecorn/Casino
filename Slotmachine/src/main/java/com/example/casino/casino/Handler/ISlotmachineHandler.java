package com.example.casino.casino.Handler;

import com.example.casino.casino.Request.ISlotmachineRequest;
import com.example.casino.casino.Response.ISlotmachineResponse;
import com.example.casino.casino.Utility.ErrorWrapper;
import com.example.casino.casino.Utility.Result;

public interface ISlotmachineHandler {

    Result<ISlotmachineResponse, ErrorWrapper> play(ISlotmachineRequest request);
}
