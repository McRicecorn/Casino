package de.casino.banking_service.user.Response;

import java.math.BigDecimal;

public interface IUserResponse {

    String firstName();
    String lastName();
    BigDecimal balance();
}
