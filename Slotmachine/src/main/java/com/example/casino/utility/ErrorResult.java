package com.example.casino.utility;

import java.util.Optional;

public class ErrorResult<FailureType> {
    private final boolean success;
    private final FailureType failureData;

    private ErrorResult(boolean success, FailureType failureData) {
        this.success = success;
        this.failureData = failureData;
    }

    public static <F> ErrorResult<F> success() {
        return new ErrorResult<>(true, null);
    }


    public static <F> ErrorResult<F> failure(F error) {
        return new ErrorResult<>(false, error);
    }


    public boolean isSuccess() {
        return success;
    }

    public boolean isFailure() {
        return !success;
    }

    public Optional<FailureType> getFailureData() {
        return Optional.ofNullable(failureData);
    }



}
