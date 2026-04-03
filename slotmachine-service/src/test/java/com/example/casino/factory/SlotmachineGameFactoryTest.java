package com.example.casino.factory;

import com.example.casino.model.ISlotmachineGameEntity;
import com.example.casino.request.ISlotmachineRequest;
import com.example.casino.utility.ErrorWrapper;
import com.example.casino.utility.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SlotmachineGameFactoryTest {
    private SlotmachineGameFactory factory;

    //static values
    private static long staticUserId = 1L;
    private static BigDecimal staticBet = BigDecimal.TEN;
    private static BigDecimal staticWin = BigDecimal.ZERO;
    private static boolean staticIsWinning = false;
    private static String staticResult = "LEMON, CHERRY, BELL";

    //dynamic values
    private long randomUserId;
    private BigDecimal randomBet;

    @BeforeEach
    void setUp() {
        factory = new SlotmachineGameFactory();
        randomUserId = (long) (Math.random() * 1000);
        randomBet = BigDecimal.valueOf(Math.random() * 100);
    }

    @Test
    @DisplayName("Should create entity and calculate winnings correctly over multiple runs")
    void createSlotmachine_Success() {
        //test
        Result<ISlotmachineGameEntity, ErrorWrapper> result = factory.createSlotmachine(staticUserId, staticBet, staticWin, staticIsWinning, staticResult);

        //assert
        assertTrue(result.isSuccess(), "Result should be a success");
        ISlotmachineGameEntity entity = result.getSuccessData().get();

        assertEquals(staticUserId, entity.getUserId());
        assertEquals(staticBet, entity.getBetAmount());
        assertEquals(staticWin, entity.getWinAmount());
        assertEquals(staticIsWinning, entity.isWinning());
        assertEquals(staticResult, entity.getSlotResult());
        assertNotNull(entity.getTimestamp(), "Timestamp should be set by the factory");
    }
}