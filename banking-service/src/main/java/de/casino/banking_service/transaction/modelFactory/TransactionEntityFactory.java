package de.casino.banking_service.transaction.modelFactory;

import de.casino.banking_service.transaction.model.ITransactionEntity;
import de.casino.banking_service.transaction.model.TransactionEntity;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import de.casino.banking_service.transaction.utility.Result;

import java.math.BigDecimal;

public class TransactionEntityFactory implements ITransactionEntityFactory{



    @Override
    public Result<ITransactionEntity, ErrorWrapper> create( BigDecimal amount, String invoicingParty, Long userId) {
        return TransactionEntity.create(amount,invoicingParty,userId);
    }
}
