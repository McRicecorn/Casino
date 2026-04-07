package de.casino.banking_service.transaction.response.transactionResponse;


import de.casino.banking_service.transaction.utility.Games;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;

@Schema(description = "Response for a Transaction")
public class DeleteTransactionResponse implements ITransactionResponse{

    @Schema (description = "the unique identifier of a user ")
    private long userId;

    @Schema (description = "the amoount of a transaction")
    private BigDecimal amount;

    @Schema (description = "the known invoincing Party")
    @Enumerated(EnumType.STRING)
    private Games invoicingParty;

    public DeleteTransactionResponse(long user_id, BigDecimal amount, Games invoicingParty){
        this.userId = user_id;
        this.amount = amount;
        this.invoicingParty = invoicingParty;
    }


    public long getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Games getInvoicingParty(){
        return invoicingParty;
    };
}
