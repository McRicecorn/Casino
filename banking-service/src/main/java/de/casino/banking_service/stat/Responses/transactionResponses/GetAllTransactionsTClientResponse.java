package de.casino.banking_service.stat.Responses.transactionResponses;


import de.casino.banking_service.common.Games;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Response for a Transaction")
public class GetAllTransactionsTClientResponse {
    @Schema(description = "the Unique identifier of a transaction")
    private long id;

    @Schema (description = "the unique identifier of a user ")
    private long userId;

    @Schema (description = "the amoount of a transaction")
    private BigDecimal amount;

    private Games invoicingParty;



    public GetAllTransactionsTClientResponse(long id, long user_id, BigDecimal amount, Games invoicingParty) {
        this.id = id;
        this.userId = user_id;
        this.amount = amount;
        this.invoicingParty = invoicingParty;

    }


    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Games getInvoicingParty() {
        return invoicingParty;
    }
}
