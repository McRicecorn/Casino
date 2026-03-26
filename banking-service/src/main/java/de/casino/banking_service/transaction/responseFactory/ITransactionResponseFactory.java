package de.casino.banking_service.transaction.responseFactory;

import de.casino.banking_service.transaction.model.ITransactionEntity;
import de.casino.banking_service.transaction.response.ITransactionResponse;

public interface ITransactionResponseFactory {

    ITransactionResponse createGetAll(ITransactionEntity entity);

    ITransactionResponse createGetByUser(ITransactionEntity entity);

    ITransactionResponse createPost(ITransactionEntity entity);

    ITransactionResponse createPut(ITransactionEntity entity);

    ITransactionResponse createDelete(ITransactionEntity entity);
}
