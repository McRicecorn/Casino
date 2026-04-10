package de.casino.banking_service.user.Response.TransactionResponse;

import de.casino.banking_service.common.Games;
import de.casino.banking_service.transaction.response.transactionResponse.ITransactionResponse;

import java.math.BigDecimal;
import java.util.List;

public class DeleteAllTransactionsClient implements ITransactionResponse {

    private int numberOfDeletedTransactions;
    private BigDecimal totalAmountLost;
    private BigDecimal totalAmountGained;
    private List<Games> invoicingPartys;

    public DeleteAllTransactionsClient(int numberOfDeletedTransactions, BigDecimal totalAmountLost, BigDecimal totalAmountGained, List<Games> invoicingPartys) {
        this.numberOfDeletedTransactions = numberOfDeletedTransactions;
        this.totalAmountLost = totalAmountLost;
        this.totalAmountGained = totalAmountGained;
        this.invoicingPartys = invoicingPartys;
    }

    public int getNumberOfDeletedTransactions() {
        return numberOfDeletedTransactions;
    }

    public BigDecimal getTotalAmountLost() {
        return totalAmountLost;
    }

    public BigDecimal getTotalAmountGained() {
        return totalAmountGained;
    }

    public List<Games> getInvoicingPartys() {
        return invoicingPartys;
    }





}
