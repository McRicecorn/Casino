package de.casino.banking_service.transaction.request;

import java.math.BigDecimal;

public interface ITransactionRequest {

    Long getId();
    Long getUserId();
    String getInvoicingParty();

    BigDecimal getAmount();
}
