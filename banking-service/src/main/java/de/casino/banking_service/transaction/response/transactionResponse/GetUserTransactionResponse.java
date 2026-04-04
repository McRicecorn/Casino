package de.casino.banking_service.transaction.response.transactionResponse;


import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Response for a Transaction")
public class GetUserTransactionResponse implements ITransactionResponse{
    @Schema(description = "the Unique identifier of a transaction")
    private long id;


    @Schema (description = "the amoount of a transaction")
    private BigDecimal amount;


    public GetUserTransactionResponse(long id, BigDecimal amount){
        this.id = id;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getamount() {
        return amount;
    }

}
