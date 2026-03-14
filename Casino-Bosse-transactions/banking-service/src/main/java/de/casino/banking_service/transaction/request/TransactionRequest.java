package de.casino.banking_service.transaction.request;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.casino.banking_service.user.model.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Request to create or update a circle.")
public class TransactionRequest implements ITransactionRequest {


    @Schema(description = "the Unique identifier of a transaction")
    private long id;

    @Schema
    private BigDecimal amount;

    @Schema (description = "the unique identifier of a user ")
    private UserEntity user;

    @Schema (description = "the known invoincing Party")
    private String invcoingParty;


    @JsonCreator
    public TransactionRequest(@JsonProperty("amount") BigDecimal amount, @JsonProperty("invoicingParty") String invcoingParty){
        this.amount=amount;
        this.invcoingParty=invcoingParty;
    }


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public UserEntity getUser() {
        return user;
    }

    @Override
    public String getInvoicingParty() {
        return invcoingParty;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }
}
