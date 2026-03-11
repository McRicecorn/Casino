package de.casino.banking_service.user.model;

import java.math.BigDecimal;

public interface IUserEntity {
        Long getId();
        String getFirstName();
        String getLastName();
        BigDecimal getBalance();
        void withdraw(BigDecimal amount);
        void deposit(BigDecimal amount);
        void rename(String firstName, String lastName);


}
