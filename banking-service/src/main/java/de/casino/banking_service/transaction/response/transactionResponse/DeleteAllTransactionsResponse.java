package de.casino.banking_service.transaction.response.transactionResponse;

import de.casino.banking_service.transaction.utility.Games;

import java.math.BigDecimal;
import java.util.List;

public class DeleteAllTransactionsResponse implements ITransactionResponse{

    private int numberOfDeletedTransactions;
    private BigDecimal totalAmountLost;
    private BigDecimal totalAmountGained;
    private List<Games> invoicingPartys;

    public DeleteAllTransactionsResponse(int numberOfDeletedTransactions, BigDecimal totalAmountLost, BigDecimal totalAmountGained, List<Games> invoicingPartys) {
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
