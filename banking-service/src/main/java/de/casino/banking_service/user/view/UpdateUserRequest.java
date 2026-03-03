package de.casino.banking_service.user.view;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateUserRequest (
    @JsonProperty("first_name") String firstName,
    @JsonProperty("last_name") String lastName
    )
{}