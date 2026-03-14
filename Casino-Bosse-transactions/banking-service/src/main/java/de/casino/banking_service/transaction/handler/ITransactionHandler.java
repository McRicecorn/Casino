package de.casino.banking_service.transaction.handler;

import de.casino.banking_service.transaction.model.TransactionEntity;
import de.casino.banking_service.transaction.request.ITransactionRequest;
import de.casino.banking_service.transaction.response.ITransactionResponse;
import de.casino.banking_service.transaction.utility.ErrorResult;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import de.casino.banking_service.transaction.utility.Result;

import java.math.BigDecimal;
import java.util.List;

public interface ITransactionHandler {



    Result<Iterable<ITransactionResponse>, ErrorWrapper> getAllTransactions();

    Result<ITransactionResponse, ErrorWrapper> createTransactionEntity(ITransactionRequest tranctionEntity);

    Result<ITransactionResponse, ErrorWrapper> updateTransactionEntity(Long id, ITransactionRequest transactionentity);

    ErrorResult<ErrorWrapper> deleteTransactionEntity(Long id);

    List<TransactionEntity> findByUserId(Long id);
}
