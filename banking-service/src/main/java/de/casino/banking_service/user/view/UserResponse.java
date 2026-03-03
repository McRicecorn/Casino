package de.casino.banking_service.user.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record UserResponse(
        Long id,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        BigDecimal balance ){
}
