package de.casino.banking_service.transaction.response.transactionResponse;


import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Response for a Transaction")
public class GetAllTransactionResponse implements ITransactionResponse{
    @Schema(description = "the Unique identifier of a transaction")
    private long id;

    @Schema (description = "the unique identifier of a user ")
    private long userId;

    @Schema (description = "the amoount of a transaction")
    private BigDecimal amount;



    public GetAllTransactionResponse(long id, long user_id, BigDecimal amount){
        this.id = id;
        this.userId = user_id;
        this.amount = amount;

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

}
