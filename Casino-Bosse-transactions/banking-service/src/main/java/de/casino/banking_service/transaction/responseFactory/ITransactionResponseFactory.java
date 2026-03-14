package de.casino.banking_service.transaction.responseFactory;

import de.casino.banking_service.transaction.response.ITransactionResponse;
import de.casino.banking_service.transaction.model.ITransactionEntity;

public interface ITransactionResponseFactory {

    ITransactionResponse create(ITransactionEntity transaction);
}
