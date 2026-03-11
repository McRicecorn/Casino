package de.casino.banking_service.user.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest (
        @JsonProperty("first_name")
        @NotBlank(message = "First name must not be blank") String firstName,
        @JsonProperty("last_name")
        @NotBlank(message = "Lirst name must not be blank") String lastName
    )
{}