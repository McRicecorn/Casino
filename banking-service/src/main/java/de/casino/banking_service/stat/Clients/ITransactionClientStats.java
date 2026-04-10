package de.casino.banking_service.stat.Clients;

import de.casino.banking_service.stat.Responses.transactionResponses.GetAllTransactionsTClientResponse;
import de.casino.banking_service.stat.Utility.ErrorWrapper;
import de.casino.banking_service.common.Result;
import org.springframework.stereotype.Component;

@Component
public interface ITransactionClientStats {
     Result<Iterable<GetAllTransactionsTClientResponse>, ErrorWrapper> getAllTransactions();
     Result<Iterable<GetAllTransactionsTClientResponse>,ErrorWrapper> getAllTransactionsById(long userId);
}
