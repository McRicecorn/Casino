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
    private UserEntity user;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    private TransactionEntity(BigDecimal amount, String invoicingParty, UserEntity user) {
    this.amount = amount;
    this.invoicingParty = invoicingParty;
    this.user= user;
    }

    public static Result <ITransactionEntity, ErrorWrapper> create(BigDecimal amount, String invoicingParty, UserEntity user){
        var requested = new TransactionEntity(amount, invoicingParty, user);
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



    public UserEntity getUser() {
        return user;
    }

    public Long getUserId(){
        return user.getId();
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
    public ErrorResult<ErrorWrapper> setUser(UserEntity user) {
     /*   var userexists = UserRepository.findbyId(user.getId());
        if (userexists.isempty){
            return ErrorResult.failure();
        }

      */
        this.user=user;
        return ErrorResult.success();
    }


    public BigDecimal getAmount() {
        return amount;
    }
}
