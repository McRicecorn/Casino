package de.casino.banking_service.user.model;

import de.casino.banking_service.user.Utility.ErrorResult;
import de.casino.banking_service.user.Utility.ErrorWrapper;

import java.math.BigDecimal;

public interface IUserEntity {
    Long getId();
    String getFirstName();
    String getLastName();
    BigDecimal getBalance();


    ErrorResult<ErrorWrapper> deposit(BigDecimal amount);
    ErrorResult<ErrorWrapper> withdraw(BigDecimal amount);
    ErrorResult<ErrorWrapper> rename(String newFirstName, String newLastName);


}
