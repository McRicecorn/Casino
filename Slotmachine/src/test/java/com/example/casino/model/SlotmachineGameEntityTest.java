package com.example.casino.model;

import com.example.casino.utility.ErrorWrapper;
import com.example.casino.utility.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SlotmachineGameEntityTest {

    private  long userId = 1L;
    private BigDecimal validBet = BigDecimal.TEN;
    private BigDecimal invalidBet = BigDecimal.valueOf(-1);
    private BigDecimal zeroBet = BigDecimal.ZERO;
    private LocalDateTime now = LocalDateTime.now();

    @Test
    @DisplayName("Create should return success for VALID bet amount")
    void create_Success_ValidBetAmount() {
        //test
        Result<ISlotmachineGameEntity, ErrorWrapper> result =
                SlotmachineGameEntity.create(userId, validBet, BigDecimal.ZERO, false, "LEMON, BELL, BAR", now);

        //assert
        assertTrue(result.isSuccess());
        ISlotmachineGameEntity entity = result.getSuccessData().get();
        assertEquals(validBet, entity.getBetAmount());
        assertEquals(userId, entity.getUserId());
    }

    @Test
    @DisplayName("Create should return Failure for INVALID bet amount < 0")
    void create_Failure_InvalidBetAmount() {
        //test
        Result<ISlotmachineGameEntity, ErrorWrapper> result =
                SlotmachineGameEntity.create(userId, invalidBet, BigDecimal.ZERO, false, "something", now);

        //assert
        assertTrue(result.isFailure());

        assertEquals(ErrorWrapper.INVALID_BET_AMOUNT, result.getFailureData().get());
    }

    @Test
    @DisplayName("Create should return Failure for Invalid slot result")
    void create_Failure_InvalidSlotResult() {
        //test
        Result<ISlotmachineGameEntity, ErrorWrapper> result =
                SlotmachineGameEntity.create(userId, validBet, BigDecimal.ZERO, false, null, now);

        //assert
        assertTrue(result.isFailure());

        assertEquals(ErrorWrapper.INVALID_SLOT_RESULT, result.getFailureData().get());
    }

    @Test
    @DisplayName("Create should return Failure for when boolean doesn't match winAmount")
    void create_Failure_InconsistentData() {
        //test
        Result<ISlotmachineGameEntity, ErrorWrapper> result =
                SlotmachineGameEntity.create(userId, validBet, BigDecimal.ZERO, true, "something", now);

        //assert
        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.INCONSISTENT_DATA, result.getFailureData().get());
    }

    @Test
    void getId_ShouldReturnCorrectValue() {
        long expectedUserId = 3647L;
        BigDecimal expectedBet = new BigDecimal("10.50");
        BigDecimal expectedWin = new BigDecimal("21.00");
        boolean expectedIsWinning = true;
        String expectedResult = "LEMON, LEMON, CHERRY";
        LocalDateTime expectedTimestamp = LocalDateTime.of(2026, 3, 11, 12, 0);

        ISlotmachineGameEntity entity = SlotmachineGameEntity.create(
                expectedUserId, expectedBet, expectedWin, expectedIsWinning, expectedResult, expectedTimestamp
        ).getSuccessData().get();

        //assert
        assertNotNull(entity);
        assertEquals(0, entity.getId(),"The ID should be 0 before the entity is saved to the database.");
    }

    @Test
    void getUserId_ShouldReturnCorrectValue() {
        long expectedUserId = 4;
        BigDecimal expectedBet = new BigDecimal("10.50");
        BigDecimal expectedWin = new BigDecimal("21.00");
        boolean expectedIsWinning = true;
        String expectedResult = "LEMON, LEMON, CHERRY";
        LocalDateTime expectedTimestamp = LocalDateTime.of(2026, 3, 11, 12, 0);

        ISlotmachineGameEntity entity = SlotmachineGameEntity.create(
                expectedUserId, expectedBet, expectedWin, expectedIsWinning, expectedResult, expectedTimestamp
        ).getSuccessData().get();

        //assert
        assertNotNull(entity);
        assertEquals(expectedUserId, entity.getUserId());
    }

    @Test
    void getBetAmount_ShouldReturnCorrectValue() {
        long expectedUserId = 55;
        BigDecimal expectedBet = new BigDecimal("10.50");
        BigDecimal expectedWin = new BigDecimal("21.00");
        boolean expectedIsWinning = true;
        String expectedResult = "LEMON, LEMON, CHERRY";
        LocalDateTime expectedTimestamp = LocalDateTime.of(2026, 3, 11, 12, 0);

        ISlotmachineGameEntity entity = SlotmachineGameEntity.create(
                expectedUserId, expectedBet, expectedWin, expectedIsWinning, expectedResult, expectedTimestamp
        ).getSuccessData().get();

        //assert
        assertNotNull(entity);
        assertEquals(expectedBet, entity.getBetAmount());
    }

    @Test
    void getWinAmount_ShouldReturnCorrectValue() {
        long expectedUserId = 3;
        BigDecimal expectedBet = new BigDecimal("10.50");
        BigDecimal expectedWin = new BigDecimal("21.00");
        boolean expectedIsWinning = true;
        String expectedResult = "LEMON, LEMON, CHERRY";
        LocalDateTime expectedTimestamp = LocalDateTime.of(2026, 3, 11, 12, 0);

        ISlotmachineGameEntity entity = SlotmachineGameEntity.create(
                expectedUserId, expectedBet, expectedWin, expectedIsWinning, expectedResult, expectedTimestamp
        ).getSuccessData().get();

        //assert
        assertNotNull(entity);
        assertEquals(expectedWin, entity.getWinAmount());
    }

    @Test
    void isWinning_ShouldReturnCorrectValue() {
        long expectedUserId = 8;
        BigDecimal expectedBet = new BigDecimal("10.50");
        BigDecimal expectedWin = new BigDecimal("21.00");
        boolean expectedIsWinning = true;
        String expectedResult = "LEMON, LEMON, CHERRY";
        LocalDateTime expectedTimestamp = LocalDateTime.of(2026, 3, 11, 12, 0);

        ISlotmachineGameEntity entity = SlotmachineGameEntity.create(
                expectedUserId, expectedBet, expectedWin, expectedIsWinning, expectedResult, expectedTimestamp
        ).getSuccessData().get();

        //assert
        assertNotNull(entity);
        assertEquals(expectedIsWinning, entity.isWinning());
    }

    @Test
    void getSlotResult_ShouldReturnCorrectValue() {
        long expectedUserId = 27532;
        BigDecimal expectedBet = new BigDecimal("10.50");
        BigDecimal expectedWin = new BigDecimal("21.00");
        boolean expectedIsWinning = true;
        String expectedResult = "LEMON, LEMON, CHERRY";
        LocalDateTime expectedTimestamp = LocalDateTime.of(2026, 3, 11, 12, 0);

        ISlotmachineGameEntity entity = SlotmachineGameEntity.create(
                expectedUserId, expectedBet, expectedWin, expectedIsWinning, expectedResult, expectedTimestamp
        ).getSuccessData().get();

        //test
        var slotResult = entity.getSlotResult();

        //assert
        assertNotNull(entity);
        assertEquals(expectedResult, slotResult);
    }

    @Test
    void getTimestamp_ShouldReturnCorrectValue() {
        long expectedUserId = 386;
        BigDecimal expectedBet = new BigDecimal("10.50");
        BigDecimal expectedWin = new BigDecimal("21.00");
        boolean expectedIsWinning = true;
        String expectedResult = "LEMON, LEMON, CHERRY";
        LocalDateTime expectedTimestamp = LocalDateTime.of(2026, 3, 11, 12, 0);

        ISlotmachineGameEntity entity = SlotmachineGameEntity.create(
                expectedUserId, expectedBet, expectedWin, expectedIsWinning, expectedResult, expectedTimestamp
        ).getSuccessData().get();

        //assert
        assertNotNull(entity);
        assertEquals(expectedTimestamp, entity.getTimestamp());
    }

    @Test
    @DisplayName("Setting a valid betAmount updates the game entity")
    void setBetAmount_Valid_ShouldUpdateBetAmount() {
        long expectedUserId = 35;
        BigDecimal expectedBet = new BigDecimal("10.50");
        BigDecimal expectedWin = new BigDecimal("21.00");
        boolean expectedIsWinning = true;
        String expectedResult = "LEMON, LEMON, CHERRY";
        LocalDateTime expectedTimestamp = LocalDateTime.of(2026, 3, 11, 12, 0);

        ISlotmachineGameEntity entity = SlotmachineGameEntity.create(
                expectedUserId, expectedBet, expectedWin, expectedIsWinning, expectedResult, expectedTimestamp
        ).getSuccessData().get();

        //assert
        assertNotNull(entity);

        BigDecimal newBet = BigDecimal.valueOf(50);
        var result = entity.setBetAmount(newBet);
        assertTrue(result.isSuccess());
        assertEquals(newBet, entity.getBetAmount());
    }

    @Test
    @DisplayName("Setting an invalid betAmount returns an error.")
    void setBetAmount_Invalid_ShouldReturnError() {
        long expectedUserId = 35;
        BigDecimal expectedBet = new BigDecimal("10.50");
        BigDecimal expectedWin = new BigDecimal("21.00");
        boolean expectedIsWinning = true;
        String expectedResult = "LEMON, LEMON, CHERRY";
        LocalDateTime expectedTimestamp = LocalDateTime.of(2026, 3, 11, 12, 0);

        ISlotmachineGameEntity entity = SlotmachineGameEntity.create(
                expectedUserId, expectedBet, expectedWin, expectedIsWinning, expectedResult, expectedTimestamp
        ).getSuccessData().get();

        //assert not null
        assertNotNull(entity);

        //negative bet amount
        BigDecimal newBet = BigDecimal.valueOf(-10);
        var result = entity.setBetAmount(newBet);

        //assert true and equal
        assertTrue(result.isFailure());
        assertEquals(expectedBet, entity.getBetAmount());
        assertEquals(ErrorWrapper.INVALID_BET_AMOUNT, result.getFailureData().get());
    }

    @Test
    @DisplayName("Protected constructor should be accessible for JPA/Frameworks")
    void protectedConstructor_ShouldExist() {
        SlotmachineGameEntity entity = new SlotmachineGameEntity();

        assertNotNull(entity, "JPA requires a no-args constructor");
        assertEquals(0, entity.getId());
    }
}