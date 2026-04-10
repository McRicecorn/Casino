package de.casino.banking_service.transaction.modelFactory;

import de.casino.banking_service.common.Games;
import de.casino.banking_service.common.Result;
import de.casino.banking_service.transaction.model.ITransactionEntity;
import de.casino.banking_service.transaction.utility.ErrorWrapper;

import java.math.BigDecimal;

public interface ITransactionEntityFactory {
    Result<ITransactionEntity, ErrorWrapper> create (BigDecimal amount, Games invoicingParty, long userId);
}
