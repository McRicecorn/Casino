package de.casino.banking_service.transaction.UserClient;

import de.casino.banking_service.transaction.response.userResponse.GetUserClientResponse;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import de.casino.banking_service.transaction.utility.Result;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface IUserClient {

    Result<GetUserClientResponse, ErrorWrapper> getUserById(Long userId);
    Result<Void, ErrorWrapper> deposit(Long userId, BigDecimal amount);
    Result<Void, ErrorWrapper> withdraw(Long userId, BigDecimal amount);

}
