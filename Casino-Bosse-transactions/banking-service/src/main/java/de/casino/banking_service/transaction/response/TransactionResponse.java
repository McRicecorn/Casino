package de.casino.banking_service.transaction.response;


import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response for a Transaction")
public class TransactionResponse implements ITransactionResponse{
    @Schema(description = "the Unique identifier of a transaction")
    private long id;

    @Schema (description = "the unique identifier of a user ")
    private long userId;

    @Schema (description = "the amoount of a transaction")
    private BigDecimal amount;

    @Schema (description = "the known invoincing Party")
    private String invcoingParty;

    public TransactionResponse(long id, long user_id, BigDecimal amount, String invoicingParty){
        this.id = id;
        this.userId = user_id;
        this.amount = amount;
        this.invcoingParty = invoicingParty;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public long getuser_id() {
        return 0;
    }

    @Override
    public BigDecimal getamount() {
        return null;
    }

    @Override
    public String getInvoicingParty(){
        return null;
    };
}
