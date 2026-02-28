package de.casino.banking_service.transaction.view;

import java.math.BigDecimal;

public class UpdateTransactionRequest {
    public String invoicing_party;
    public Long user;
    public BigDecimal amount;
}
