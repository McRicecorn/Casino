package de.casino.banking_service.user.model;

import de.casino.banking_service.user.Utility.ErrorResult;
import de.casino.banking_service.user.Utility.ErrorWrapper;
import de.casino.banking_service.user.Utility.Result;
import de.casino.banking_service.user.exceptions.InvalidAmountException;
import de.casino.banking_service.user.exceptions.InvalidUserDataException;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity //erstellt eine Tabelle in der Datenbank
@Table(name = "users") // legt den Tabellennamen fest
public class UserEntity implements IUserEntity{


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

        //validateName(firstName, lastName);
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.balance = BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY); // Startbalance
    }
    public static Result<IUserEntity, ErrorWrapper> create(String firstName, String lastName) {

        var requested = new UserEntity(firstName, lastName);
        var isNameValid = requested.isNameNotEmpty(firstName);
        var isLastNameValid = requested.isNameNotEmpty(lastName);
        if (isNameValid.isFailure() || isLastNameValid.isFailure()) {
            return Result.failure(
                    isNameValid.isFailure() ? isNameValid.getFailureData().orElse(ErrorWrapper.UNEXPECTED_INTERNAL_ERROR_OCCURED) :
                            isLastNameValid.getFailureData().orElse(ErrorWrapper.UNEXPECTED_INTERNAL_ERROR_OCCURED));
        }
        return Result.success(requested);
    }



    public void rename(String newFirstName, String newLastName) {
        validateName(newFirstName, newLastName);

        this.firstName = newFirstName.trim();
        this.lastName = newLastName.trim();
    }

    public void withdraw(BigDecimal amount) {
        validateAmount(amount);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }
        if (balance.compareTo(amount) <0) {
            throw new InvalidAmountException("Insufficient funds");
        }

        this.balance = this.balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        validateAmount(amount);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }
        this.balance = this.balance.add(amount);
    }

    //helprer methods
    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new InvalidAmountException("Amount must not be null");
        }

        if (amount.scale() > 2) {
            throw new InvalidAmountException("Amount must have no more than 2 decimal places");
        }



    }

    private ErrorResult<ErrorWrapper> isNameNotEmpty(String name) {
        if (name == null || name.isBlank()) {
            return ErrorResult.failure(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL);
        }
        if (name.length() > 12) {
            return ErrorResult.failure(ErrorWrapper.USER_MODEL_INVALID_NAME_LENGTH);
        }

        return ErrorResult.success();
    }

    
    private void validateName(String firstName, String lastName) {

        if (firstName == null || firstName.isBlank()) {
            throw new InvalidUserDataException("First name invalid");
        }

        if (lastName == null || lastName.isBlank()) {
            throw new InvalidUserDataException("Last name invalid");
        }

        if (this.firstName != null &&
                firstName.equals(this.firstName) &&
                lastName.equals(this.lastName)) {

            throw new InvalidUserDataException("New name must be different");
        }
    }


    // Getter
    public Long getId() { return id; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public BigDecimal getBalance() { return balance; }



}