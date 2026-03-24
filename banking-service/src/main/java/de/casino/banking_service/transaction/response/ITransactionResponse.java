package de.casino.banking_service.transaction.response;

import java.math.BigDecimal;

public interface ITransactionResponse {
    String getInvoicingParty();
    long getId();
    long getuser_id();
    BigDecimal getamount();

}
