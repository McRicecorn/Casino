package de.casino.banking_service.transaction.handler;

import de.casino.banking_service.transaction.request.ITransactionRequest;
import de.casino.banking_service.transaction.response.ITransactionResponse;
import de.casino.banking_service.transaction.utility.ErrorResult;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import de.casino.banking_service.transaction.utility.Result;

public interface ITransactionHandler {



    Result<Iterable<ITransactionResponse>, ErrorWrapper> getAllTransactions();

    Result<ITransactionResponse, ErrorWrapper> createTransactionEntity(ITransactionRequest tranctionEntity, Long userId);

    Result<ITransactionResponse, ErrorWrapper> updateTransactionEntity(Long id, ITransactionRequest transactionentity);

    ErrorResult<ErrorWrapper> deleteTransactionEntity(Long id);

    Result<Iterable<ITransactionResponse>, ErrorWrapper> findByUserId(Long id);
}
