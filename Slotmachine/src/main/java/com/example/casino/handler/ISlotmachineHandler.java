package com.example.casino.handler;

import com.example.casino.dto.stats.IGlobalStatsResponse;
import com.example.casino.dto.stats.IUserStatsResponse;
import com.example.casino.model.ISlotmachineGameEntity;
import com.example.casino.request.ISlotmachineRequest;
import com.example.casino.response.ISlotmachineResponse;
import com.example.casino.response.SlotmachineResponse;
import com.example.casino.utility.ErrorWrapper;
import com.example.casino.utility.Result;

public interface ISlotmachineHandler {

    Result<ISlotmachineResponse, ErrorWrapper> play(ISlotmachineRequest request);

    Result<Iterable<ISlotmachineResponse>, ErrorWrapper> readAllGames();

    Result<ISlotmachineResponse, ErrorWrapper> readGame(long id);

    String calculateChances();

    String getRules();

    Result<IUserStatsResponse, ErrorWrapper> readUserStats(long userId);

    Result<IGlobalStatsResponse, ErrorWrapper> readGlobalStats();
}
