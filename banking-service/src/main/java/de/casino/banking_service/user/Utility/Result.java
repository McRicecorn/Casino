package de.casino.banking_service.user.Utility;

import java.util.Optional;

public class Result <SuccessType, FailureType>{

    private final boolean success;
    private final SuccessType successData;
    private final FailureType failureData;


    private Result(boolean success, SuccessType successData, FailureType failureData) {
        this.success = success;
        this.successData = successData;
        this.failureData = failureData;
    }


    public static <S, F> Result<S, F> success(S data) {
        return new Result<>(true, data, null);
    }


    public static <S, F> Result<S, F> failure(F error) {
        return new Result<>(false, null, error);
    }


    public boolean isSuccess() {
        return success;
    }


    public boolean isFailure() {
        return !success;
    }


    public Optional<SuccessType> getSuccessData() {
        return Optional.ofNullable(successData);
    }

    public Optional<FailureType> getFailureData() {
        return Optional.ofNullable(failureData);
    }


}