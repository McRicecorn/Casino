package de.casino.banking_service.transaction.responseFactory;

import de.casino.banking_service.transaction.model.ITransactionEntity;
import de.casino.banking_service.transaction.response.ITransactionResponse;
import de.casino.banking_service.transaction.response.TransactionResponse;
import org.springframework.stereotype.Component;

@Component
public class TransactionResponseFactory implements ITransactionResponseFactory {

    @Override
    public ITransactionResponse create(ITransactionEntity transactionEntity) {
    return new TransactionResponse(transactionEntity.getId(),transactionEntity.getUserId(), transactionEntity.getAmount(), transactionEntity.getInvoicingParty());
}



}
