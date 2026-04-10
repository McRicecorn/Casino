package de.casino.banking_service.transaction.modelFactory;

import de.casino.banking_service.common.Games;
import de.casino.banking_service.common.Result;
import de.casino.banking_service.transaction.model.ITransactionEntity;
import de.casino.banking_service.transaction.model.TransactionEntity;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public class TransactionEntityFactory implements ITransactionEntityFactory{



    @Override
    public Result<ITransactionEntity, ErrorWrapper> create(BigDecimal amount, Games invoicingParty, long userId) {
        return TransactionEntity.create(amount,invoicingParty,userId);
    }
}
