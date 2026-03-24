package de.casino.banking_service.transaction.modelFactory;

import ch.qos.logback.core.recovery.ResilientFileOutputStream;
import de.casino.banking_service.transaction.model.ITransactionEntity;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import de.casino.banking_service.transaction.utility.Result;
import de.casino.banking_service.user.model.UserEntity;

import java.math.BigDecimal;

public interface ITransactionEntityFactory {
    Result<ITransactionEntity, ErrorWrapper> create ( BigDecimal amount, String invoicingParty, Long userId);
}
