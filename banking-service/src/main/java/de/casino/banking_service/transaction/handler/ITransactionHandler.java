package de.casino.banking_service.transaction.handler;

import de.casino.banking_service.transaction.request.ITransactionRequest;
import de.casino.banking_service.transaction.request.PostTransactionRequest;
import de.casino.banking_service.transaction.request.PutTransactionRequest;
import de.casino.banking_service.transaction.response.ITransactionResponse;
import de.casino.banking_service.transaction.utility.ErrorResult;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import de.casino.banking_service.transaction.utility.Result;

public interface ITransactionHandler {

   Result<Iterable<ITransactionResponse>, ErrorWrapper> getAllTransactions();
   Result<Iterable<ITransactionResponse>, ErrorWrapper> getTransactionsByUserId(Long id);
   Result<ITransactionResponse, ErrorWrapper> createTransaction(PostTransactionRequest request, long userId);
   Result<ITransactionResponse, ErrorWrapper> updateTransaction(Long id, PutTransactionRequest request);
   Result<ITransactionResponse, ErrorWrapper> deleteTransaction(Long id);


   }
