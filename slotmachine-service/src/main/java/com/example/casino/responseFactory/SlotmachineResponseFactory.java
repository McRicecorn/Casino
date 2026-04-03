package com.example.casino.responseFactory;

import com.example.casino.dto.stats.GlobalStatsResponse;
import com.example.casino.dto.stats.IGlobalStatsResponse;
import com.example.casino.dto.stats.IUserStatsResponse;
import com.example.casino.dto.stats.UserStatsResponse;
import com.example.casino.model.ISlotmachineGameEntity;
import com.example.casino.response.ISlotmachineResponse;
import com.example.casino.response.SlotmachineResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;


@Component
public class SlotmachineResponseFactory implements ISlotmachineResponseFactory {

    @Override
    public ISlotmachineResponse createSlotmachineResponse(ISlotmachineGameEntity gameEntity) {

        String message;
        if (gameEntity.isWinning()) {
            if (gameEntity.getWinAmount().compareTo(gameEntity.getBetAmount().multiply(BigDecimal.TEN)) >= 0) {
                message = "FANTASTIC! That was an epic WIN! Congrats \uD83E\uDD73";
            } else {
                message = "You WON! Your bank balance will thank you.";
            }
        } else {
            message = "Nothing won \uD83D\uDE22❌\uD83D\uDE35. Better luck next time! \uD83E\uDD1E";
        }

        return new SlotmachineResponse(
                gameEntity.getId(),
                gameEntity.getUserId(),
                gameEntity.getWinAmount().subtract(gameEntity.getBetAmount()),
                gameEntity.isWinning(),
                gameEntity.getSlotResult(),
                message);
    }

    @Override
    public IUserStatsResponse createUserStatsResponse(List<ISlotmachineGameEntity> userGames) {

        long userId = userGames.get(0).getUserId();

        int totalGamesCount = userGames.size();

        int totalWinnings = (int) userGames.stream()
                .filter(ISlotmachineGameEntity::isWinning)
                .count();

        int totalLosses = (int) userGames.stream()
                .filter(g -> !g.isWinning())
                .count();

        BigDecimal totalTurnover = userGames.stream()
                .map(ISlotmachineGameEntity::getBetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal winningsSum = userGames.stream()
                .map(ISlotmachineGameEntity::getWinAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalClientProfit = winningsSum.subtract(totalTurnover);

        BigDecimal totalHouseProfit = totalTurnover.subtract(winningsSum);


        return new UserStatsResponse(userId, totalGamesCount, totalWinnings, totalLosses, totalClientProfit, totalTurnover, totalHouseProfit);
    }

    public IGlobalStatsResponse createGlobalStatsResponse(List<? extends ISlotmachineGameEntity> allGames) {

        long totalClientCount = allGames.stream().map(g -> g.getUserId()).distinct().count();

        long totalGameCount = allGames.size();

        BigDecimal totalTurnover = allGames.stream()
                .map(ISlotmachineGameEntity::getBetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCashOut = allGames.stream()
                .map(ISlotmachineGameEntity::getWinAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalProfit = totalTurnover.subtract(totalCashOut);

        return new GlobalStatsResponse(totalClientCount, totalGameCount, totalProfit, totalCashOut, totalTurnover);
    }
}
