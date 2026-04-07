package de.casino.banking_service.transaction.request;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.casino.banking_service.transaction.utility.Games;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Request to create or update a circle.")
public class PostTransactionRequest implements ITransactionRequest {

    @Schema
    private final BigDecimal amount;



    @Schema (description = "the known invoincing Party")
    private final Games invoicingParty;


    @JsonCreator
    public PostTransactionRequest(@JsonProperty("amount") BigDecimal amount, @JsonProperty("invoicingParty") Games invoicingParty){
        this.amount=amount;
        this.invoicingParty=invoicingParty;
    }


    public Games getInvoicingParty() {
        return invoicingParty;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
