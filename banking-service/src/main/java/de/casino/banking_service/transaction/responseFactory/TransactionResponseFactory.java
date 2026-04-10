package de.casino.banking_service.transaction.responseFactory;

import de.casino.banking_service.common.Games;
import de.casino.banking_service.transaction.model.ITransactionEntity;
import de.casino.banking_service.transaction.response.transactionResponse.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class TransactionResponseFactory implements ITransactionResponseFactory {

    @Override
    public ITransactionResponse createGetAll(ITransactionEntity t) {
        return new GetAllTransactionResponse(
                t.getTransactionId(),
                t.getUserId(),
                t.getAmount(),
                t.getInvoicingParty()
        );
    }

    @Override
    public ITransactionResponse createGetByUser(ITransactionEntity t) {
        return new GetUserTransactionResponse(
                t.getTransactionId(),
                t.getAmount()
        );
    }

    @Override
    public ITransactionResponse createPost(ITransactionEntity t) {
        return new PostTransactionResponse(
                t.getTransactionId(),
                t.getUserId(),
                t.getAmount(),
                t.getInvoicingParty()
        );
    }

    @Override
    public ITransactionResponse createPut(ITransactionEntity t) {
        return new PutTransactionResponse(
                t.getTransactionId(),
                t.getUserId(),
                t.getAmount(),
                t.getInvoicingParty()
        );
    }

    @Override
    public ITransactionResponse createDelete(ITransactionEntity t) {
        return new DeleteTransactionResponse(
                t.getUserId(),
                t.getAmount(),
                t.getInvoicingParty()
        );
    }

    @Override
    public ITransactionResponse createDeleteAll(int numberOfDeletedTransactions, BigDecimal totalAmountLost, BigDecimal totalAmountGained, List<Games> invoicingPartys) {
        return new DeleteAllTransactionsResponse(
                numberOfDeletedTransactions,
                totalAmountLost,
                totalAmountGained,
                invoicingPartys
        );
    }
}