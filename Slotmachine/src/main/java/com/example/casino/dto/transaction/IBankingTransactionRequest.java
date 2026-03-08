package com.example.casino.dto.transaction;

public interface IBankingTransactionRequest {

    String getInvoicingParty();
    void setInvoicingParty(String invoicingParty);
    double getAmount();
    void setAmount(double amount);
}
