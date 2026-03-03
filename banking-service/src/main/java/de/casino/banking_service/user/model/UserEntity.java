package de.casino.banking_service.user.model;

import de.casino.banking_service.user.exceptions.InvalidUserDataException;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity //erstellt eine Tabelle in der Datenbank
@Table(name = "users") // legt den Tabellennamen fest
public class UserEntity {

    @Id //makiert primärschlüssel
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto inkrement in db
    private Long id;

    @Column(nullable = false, length = 12) //erlaubt keine Nullwerte
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    public UserEntity() {
        // Default-Konstruktor für JPA
    }

    public UserEntity(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank()) {
            throw new InvalidUserDataException("First name invalid");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new InvalidUserDataException("Last name invalid");
        }
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.balance = BigDecimal.ZERO.setScale(2); // Startbalance
    }



    public void rename(String newFirstName, String newLastName) {
        validateName(newFirstName.trim(), newLastName.trim());

        this.firstName = newFirstName.trim();
        this.lastName = newLastName.trim();
    }

    public void withdraw(BigDecimal amount) {
        validateAmount(amount);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidUserDataException("Amount must be positive");
        }
        if (balance.compareTo(amount) < 0) {
            throw new InvalidUserDataException("Insufficient funds");
        }

        this.balance = this.balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        validateAmount(amount);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidUserDataException("Amount must be positive");
        }
        this.balance = this.balance.add(amount);
    }

    //helprer methods
    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new InvalidUserDataException("Amount must not be null");
        }
        if (amount.equals(BigDecimal.ZERO)) {
            throw new InvalidUserDataException("Amount is zero");
        }
        if (amount.scale() > 2) {
            throw new InvalidUserDataException("Amount must have no more than 2 decimal places");
        }



    }

    private void validateName(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank()) {
            throw new InvalidUserDataException("First name invalid");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new InvalidUserDataException("Last name invalid");
        }
        if (firstName.equals(this.firstName) && lastName.equals(this.lastName)) {
            throw new InvalidUserDataException("New name must be different from the current name");
        }
    }


    // Getter
    public Long getId() { return id; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public BigDecimal getBalance() { return balance; }
}