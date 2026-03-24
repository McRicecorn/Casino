package de.casino.banking_service.transaction.request;

import de.casino.banking_service.user.model.UserEntity;
import org.apache.catalina.User;

import java.math.BigDecimal;

public interface ITransactionRequest {

    Long getId();
    Long getUserId();
    String getInvoicingParty();

    BigDecimal getAmount();
}
