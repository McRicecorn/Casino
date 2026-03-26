package de.casino.banking_service.transaction.modelFactory;

import de.casino.banking_service.transaction.model.ITransactionEntity;
import de.casino.banking_service.transaction.model.TransactionEntity;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import de.casino.banking_service.transaction.utility.Games;
import de.casino.banking_service.transaction.utility.Result;
import de.casino.banking_service.user.model.IUserEntity;
import de.casino.banking_service.user.model.UserEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public class TransactionEntityFactory implements ITransactionEntityFactory{



    @Override
    public Result<ITransactionEntity, ErrorWrapper> create(BigDecimal amount, Games invoicingParty, UserEntity userId) {
        return TransactionEntity.create(amount,invoicingParty,userId);
    }
}
