package de.casino.banking_service.transaction.response.userResponse;

import com.fasterxml.jackson.annotation.JsonProperty;


import java.math.BigDecimal;

public class GetUserClientResponse{
    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private BigDecimal balance;

    public GetUserClientResponse() {}

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getBalance() {
        return balance;
    }


}