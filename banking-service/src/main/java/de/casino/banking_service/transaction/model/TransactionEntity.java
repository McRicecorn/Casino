package de.casino.banking_service.transaction.model;

import de.casino.banking_service.user.model.UserEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String invoicingParty;

    @ManyToOne(optional = false)
    private UserEntity user;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    protected TransactionEntity() {
    }

    public TransactionEntity(String invoicingParty, UserEntity user, BigDecimal amount) {
        this.invoicingParty = invoicingParty;
        this.user = user;
        this.amount = amount;
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

    public BigDecimal getAmount() {
        return amount;
    }
}
