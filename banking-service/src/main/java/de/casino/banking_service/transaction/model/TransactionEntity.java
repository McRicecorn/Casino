package de.casino.banking_service.transaction.model;

import de.casino.banking_service.transaction.utility.ErrorResult;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import de.casino.banking_service.transaction.utility.Result;
import de.casino.banking_service.transaction.utility.Games;
import de.casino.banking_service.user.model.UserEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

import static de.casino.banking_service.transaction.utility.Games.ROULETTE;



//Todo: Balance bei Update ändern
//bei delete auch Balance ändern?
@Entity
@Table(name = "transactions")
public class TransactionEntity implements ITransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Games invoicingParty;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    private TransactionEntity(BigDecimal amount, Games invoicingParty, long user) {
    this.amount = amount;
    this.invoicingParty = invoicingParty;
    this.userId = user;
    }
    public TransactionEntity() {

    }
    public static Result <ITransactionEntity, ErrorWrapper> create(BigDecimal amount, Games invoicingParty, long user){
        var isInvoicingPartyValid = validateInvoicingParty(invoicingParty);
        if (isInvoicingPartyValid.isFailure()) {
            return Result.failure(isInvoicingPartyValid.getFailureData().get());
        }

        var amountValidation = validateAmount(amount);
        if (amountValidation.isFailure()) {
            return Result.failure(amountValidation.getFailureData().get());
        }
        var requested = new TransactionEntity(amount, invoicingParty, user);



            return Result.success(requested);

    }



    public ErrorResult<ErrorWrapper> update(
            BigDecimal amount,
            Games invoicingParty

    ) {
        var amountValidation = validateAmount(amount);
        if (amountValidation.isFailure()) {
            return amountValidation;
        }

        var invoicingValidation = validateInvoicingParty(invoicingParty);
        if (invoicingValidation.isFailure()) {
            return invoicingValidation;
        }

        this.amount = amount;
        this.invoicingParty = invoicingParty;


        return ErrorResult.success();
    }

    private static ErrorResult<ErrorWrapper> validateAmount(BigDecimal amount) {
        if (amount == null) {
            return ErrorResult.failure(ErrorWrapper.AMOUNT_WAS_NULL);
        }
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return ErrorResult.failure(ErrorWrapper.AMOUNT_WAS_ZERO);
        }

        if (amount.scale() > 2) {
            return ErrorResult.failure(ErrorWrapper.AMOUNT_HAS_TOO_MANY_DECIMAL_PLACES);
        }

        return ErrorResult.success();
    }
    private static ErrorResult<ErrorWrapper> validateInvoicingParty(Games invoicingParty) {
        if (invoicingParty == null) {
            return ErrorResult.failure(ErrorWrapper.INVOICING_PARTY_DOES_NOT_EXIST);
        }
        return ErrorResult.success();
    }

    public Long getTransactionId() {
        return transactionId;
    }


    public Games getInvoicingParty() {
        return invoicingParty;
    }


    public Long getUserId(){
        return userId;
    }


    public BigDecimal getAmount() {
        return amount;
    }
}
