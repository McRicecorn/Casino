package de.casino.banking_service.transaction.model;

import de.casino.banking_service.transaction.utility.ErrorResult;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import de.casino.banking_service.transaction.utility.Games;
import de.casino.banking_service.user.model.IUserEntity;
import de.casino.banking_service.user.model.UserEntity;
import org.apache.catalina.User;

import java.math.BigDecimal;

public interface ITransactionEntity {

    Long getTransactionId();
    BigDecimal getAmount();
    Games getInvoicingParty();


    ErrorResult<ErrorWrapper> update(BigDecimal amount, Games invoicingParty);
    Long getUserId();



}
