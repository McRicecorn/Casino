package de.casino.banking_service.user.TransactionClient;

import de.casino.banking_service.common.Result;
import de.casino.banking_service.user.Response.TransactionResponse.DeleteAllTransactionsClient;
import de.casino.banking_service.user.Utility.ErrorWrapper;

public interface ITransactionClient {

        Result<DeleteAllTransactionsClient, ErrorWrapper> deleteTransactionsByUserId(Long userId);

}
