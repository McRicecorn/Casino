package de.casino.banking_service.transaction.request;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.casino.banking_service.transaction.utility.Games;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Request to create or update a circle.")
public class PutTransactionRequest implements ITransactionRequest {


    @Schema
    private BigDecimal amount;

    @Schema (description = "the unique identifier of a user ")
    private Long userId;

    @Schema (description = "the known invoincing Party")
    private Games invoicingParty;


    @JsonCreator
    public PutTransactionRequest(@JsonProperty("amount") BigDecimal amount, @JsonProperty("invoicingParty") Games invoicingParty, long userId){
        this.amount=amount;
        this.invoicingParty=invoicingParty;
        this.userId = userId;
    }



    public Long getUserId() {
        return userId;
    }

    public Games getInvoicingParty() {
        return invoicingParty;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
