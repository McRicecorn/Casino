package com.example.casino.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Request to make transaction with banking service")
public class BankingTransactionRequest implements IBankingTransactionRequest {
    @JsonProperty("invoicing_party")
    private String invoicing_party;

    @JsonProperty("amount")
    private BigDecimal amount;

    public BankingTransactionRequest() {}

    public BankingTransactionRequest(String invoicing_party, BigDecimal amount) {
        this.invoicing_party = invoicing_party;
        this.amount = amount;
    }

    @Override
    public String getInvoicingParty() {
        return invoicing_party;
    }
    @Override
    public void setInvoicingParty(String invoicing_party) {
        this.invoicing_party = invoicing_party;
    }
    @Override
    public BigDecimal getAmount() {
        return amount;
    }
    @Override
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
