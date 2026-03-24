package de.casino.banking_service.transaction.model;

import de.casino.banking_service.transaction.utility.ErrorResult;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import de.casino.banking_service.transaction.utility.Result;
import de.casino.banking_service.user.model.UserEntity;
import de.casino.banking_service.user.repository.UserRepository;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
public class TransactionEntity implements ITransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String invoicingParty;

    @ManyToOne(optional = false)
    private Long userId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    private TransactionEntity(BigDecimal amount, String invoicingParty, Long userId) {
    this.amount = amount;
    this.invoicingParty = invoicingParty;
    this.userId = userId;
    }

    public static Result <ITransactionEntity, ErrorWrapper> create(BigDecimal amount, String invoicingParty, long userId){
        var requested = new TransactionEntity(amount, invoicingParty, userId);
        var isamountGreaterthanZero = requested.isamountGreaterthanZero();

        if (isamountGreaterthanZero.isFailure()){
            return Result.failure(isamountGreaterthanZero.getFailureData().orElse(ErrorWrapper.AMOUNT_WAS_NEGATIVE_OR_NULL));
        }
        else
        return Result.success(requested);
    }

    private ErrorResult<ErrorWrapper> isamountGreaterthanZero(){
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                return ErrorResult.failure(ErrorWrapper.AMOUNT_WAS_NEGATIVE_OR_NULL);
            }
            return ErrorResult.success();
    }

    public Long getId() {
        return id;
    }

    public String getInvoicingParty() {
        return invoicingParty;
    }


    public Long getUserId(){
        return userId;
    }

    @Override
    public ErrorResult<ErrorWrapper> setAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            return ErrorResult.failure(ErrorWrapper.AMOUNT_WAS_NEGATIVE_OR_NULL);
        }
        else
            return ErrorResult.success();
    }

    @Override
    public ErrorResult<ErrorWrapper> setInvoicingParty(String invoicingParty) {
        if (invoicingParty.equals("Roulette")||invoicingParty.equals("Slotmaschine")) {
            this.invoicingParty = invoicingParty;
            return ErrorResult.success();
        }
        else
            return ErrorResult.failure(ErrorWrapper.INVOICING_PARTY_DOES_NOT_EXIST);
    }

    @Override
    public ErrorResult<ErrorWrapper> setUserId(Long userId) {
        this.userId=userId;
        return ErrorResult.success();
    }


    public BigDecimal getAmount() {
        return amount;
    }
}
