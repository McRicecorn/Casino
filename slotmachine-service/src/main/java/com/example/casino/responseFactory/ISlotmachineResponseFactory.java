package com.example.casino.responseFactory;

import com.example.casino.dto.stats.IGlobalStatsResponse;
import com.example.casino.dto.stats.IUserStatsResponse;
import com.example.casino.model.ISlotmachineGameEntity;
import com.example.casino.repository.ISlotmachineRepository;
import com.example.casino.response.ISlotmachineResponse;

import java.util.List;

public interface ISlotmachineResponseFactory {
    ISlotmachineResponse createSlotmachineResponse(ISlotmachineGameEntity slotmachine);

    IUserStatsResponse createUserStatsResponse(List<ISlotmachineGameEntity> userGames);

    IGlobalStatsResponse createGlobalStatsResponse(List<? extends ISlotmachineGameEntity> allGames);
}
