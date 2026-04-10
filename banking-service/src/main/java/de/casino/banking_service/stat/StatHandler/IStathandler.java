package de.casino.banking_service.stat.StatHandler;

import de.casino.banking_service.stat.Responses.statResponses.GetGameStatsResponse;
import de.casino.banking_service.stat.Responses.statResponses.GetGlobalStatResponse;
import de.casino.banking_service.stat.Utility.ErrorWrapper;
import de.casino.banking_service.common.Result;

public interface IStathandler {

    Result<GetGlobalStatResponse, ErrorWrapper> getGlobalStatByUserId(Long userId);
    Result<GetGlobalStatResponse, ErrorWrapper> getGlobalStat();
    Result<GetGameStatsResponse, ErrorWrapper> getGameStats(String gameName , Long userId);
    Result<GetGameStatsResponse, ErrorWrapper> getGameStats(String gameName);

}
