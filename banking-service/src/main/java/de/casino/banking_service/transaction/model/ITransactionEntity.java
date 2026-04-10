package de.casino.banking_service.transaction.model;

import de.casino.banking_service.common.ErrorResult;
import de.casino.banking_service.common.Games;
import de.casino.banking_service.transaction.utility.ErrorWrapper;

import java.math.BigDecimal;

public interface ITransactionEntity {

    Long getTransactionId();
    BigDecimal getAmount();
    Games getInvoicingParty();


    ErrorResult<ErrorWrapper> update(BigDecimal amount, Games invoicingParty);
    Long getUserId();



}
