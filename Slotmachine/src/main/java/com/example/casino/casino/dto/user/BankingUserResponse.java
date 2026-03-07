package com.example.casino.casino.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BankingUserResponse implements IBankingUserResponse {
    @JsonProperty("id")
    private long userId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("balance")
    private double balance;

    public BankingUserResponse(long userId, String firstName, String lastName, double balance) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
    }

    @Override
    public long getUserId() {
        return userId;
    }
    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public double getBalance(){
        return balance;
    }
}
