package com.example.casino.dto.user;

import java.math.BigDecimal;

public interface IBankingUserResponse {
    long getUserId();
    String getFirstName();
    String getLastName();
    BigDecimal getBalance();
}
