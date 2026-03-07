package com.example.casino.casino.handler;

import com.example.casino.casino.request.ISlotmachineRequest;
import com.example.casino.casino.response.ISlotmachineResponse;
import com.example.casino.casino.utility.ErrorWrapper;
import com.example.casino.casino.utility.Result;

public interface ISlotmachineHandler {

    Result<ISlotmachineResponse, ErrorWrapper> play(ISlotmachineRequest request);

    Result<Iterable<ISlotmachineResponse>, ErrorWrapper> readAllGames();

    Result<ISlotmachineResponse, ErrorWrapper> readGame(long id);

    String calculateChances();

    String getRules();
}
