package de.casino.banking_service.transaction.responseFactory;

import de.casino.banking_service.common.Games;
import de.casino.banking_service.transaction.model.ITransactionEntity;
import de.casino.banking_service.transaction.response.transactionResponse.ITransactionResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ITransactionResponseFactory {

    ITransactionResponse createGetAll(ITransactionEntity entity);

    ITransactionResponse createGetByUser(ITransactionEntity entity);

    ITransactionResponse createPost(ITransactionEntity entity);

    ITransactionResponse createPut(ITransactionEntity entity);

    ITransactionResponse createDelete(ITransactionEntity entity);
        ITransactionResponse createDeleteAll(int numberOfDeletedTransactions, BigDecimal totalAmountLost, BigDecimal totalAmountGained, List<Games> invoicingPartys);
}
