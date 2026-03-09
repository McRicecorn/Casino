package com.example.casino.model;

import com.example.casino.utility.ErrorResult;
import com.example.casino.utility.ErrorWrapper;
import com.example.casino.utility.Result;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "slotmachine_game")
public class SlotmachineGameEntity implements ISlotmachineGameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "betAmount")
    private double betAmount;

    @Column(name = "winAmount")
    private double winAmount;

    @Column(name = "isWinning")
    private boolean isWinning;

    @Column(name = "slotResult")
    private String slotResult;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    private SlotmachineGameEntity() {}

    private SlotmachineGameEntity(long userId, double betAmount, double winAmount, boolean isWinning, String slotResult, LocalDateTime timestamp) {
        this.userId = userId;
        this.betAmount = betAmount;
        this.winAmount = winAmount;
        this.isWinning = isWinning;
        this.slotResult = slotResult;
        this.timestamp = timestamp;
    }

    public static Result<ISlotmachineGameEntity, ErrorWrapper> create(long userId, double betAmount, double winAmount, boolean isWinning, String slotResult, LocalDateTime timestamp) {
        var requested = new SlotmachineGameEntity(userId, betAmount, winAmount, isWinning, slotResult, timestamp);

        var isBetAmountValid = requested.isBetAmountValid();

        if (isBetAmountValid.isFailure()){
            return Result.failure(isBetAmountValid.getFailureData().orElse(ErrorWrapper.UNEXPECTED_INTERNAL_ERROR));
        }
        return Result.success(requested);
    }

    private ErrorResult<ErrorWrapper> isBetAmountValid() {
        //ist der Einsatz negativ? -> Fehler
        if (betAmount <= 0) {
            return ErrorResult.failure(ErrorWrapper.INVALID_BET_AMOUNT);
        }
        return ErrorResult.success();
    }


    @Override
    public long getId() {
        return id;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public double getBetAmount() {
        return betAmount;
    }

    @Override
    public double getWinAmount() {
        return winAmount;
    }

    @Override
    public boolean isWinning() {
        return isWinning;
    }

    @Override
    public String getSlotResult() {
        return slotResult;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public ErrorResult<ErrorWrapper> setBetAmount(double betAmount){
        if (betAmount <= 0) {
            return ErrorResult.failure(ErrorWrapper.INVALID_BET_AMOUNT);
        }
        this.betAmount = betAmount;
        return ErrorResult.success();
    }
}
