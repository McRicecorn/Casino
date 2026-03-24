package de.casino.banking_service.transaction.responseFactory;

import de.casino.banking_service.transaction.model.ITransactionEntity;
import de.casino.banking_service.transaction.response.ITransactionResponse;

public interface ITransactionResponseFactory {

    ITransactionResponse create(ITransactionEntity transaction);
}
