package com.example.casino.casino.responseFactory;

import com.example.casino.casino.model.ISlotmachineGameEntity;
import com.example.casino.casino.response.ISlotmachineResponse;
import com.example.casino.casino.response.SlotmachineResponse;
import org.springframework.stereotype.Component;

@Component
public class SlotmachineResponseFactory implements ISlotmachineResponseFactory {

    @Override
    public ISlotmachineResponse createSlotmachineResponse(ISlotmachineGameEntity gameEntity) {

        String message;
        if (gameEntity.isWinning()) {
            if (gameEntity.getWinAmount() >= gameEntity.getBetAmount() * 10) {
                message = "FANTASTIC! That was an epic WIN! Congrats \uD83E\uDD73";
            } else {
                message = "You WON! Your bank balance will thank you.";
            }
        } else {
            message = "Nothing won \uD83D\uDE22❌\uD83D\uDE35. Better luck next time! \uD83E\uDD1E";
        }

        return new SlotmachineResponse(
                gameEntity.getId(),
                gameEntity.getWinAmount() - gameEntity.getBetAmount(),
                gameEntity.isWinning(),
                gameEntity.getSlotResult(),
                message);
    }
}
