package de.casino.banking_service.transaction.model;

import de.casino.banking_service.transaction.utility.ErrorResult;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import de.casino.banking_service.transaction.utility.Result;
import de.casino.banking_service.user.model.UserEntity;

import java.math.BigDecimal;

public interface ITransactionEntity {

    Long getId();
    BigDecimal getAmount();
    String getInvoicingParty();

    Long getUserId();

    public ErrorResult <ErrorWrapper> setAmount(BigDecimal amount);

    public ErrorResult <ErrorWrapper> setInvoicingParty(String invoicingParty);

    public ErrorResult <ErrorWrapper> setUserId(Long userId);
}
