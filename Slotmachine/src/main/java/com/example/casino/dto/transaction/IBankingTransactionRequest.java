package com.example.casino.dto.transaction;

import java.math.BigDecimal;

public interface IBankingTransactionRequest {

    String getInvoicingParty();
    void setInvoicingParty(String invoicingParty);
    BigDecimal getAmount();
    void setAmount(BigDecimal amount);
}
