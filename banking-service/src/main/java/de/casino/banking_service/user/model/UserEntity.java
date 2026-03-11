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

    private UserEntity(String firstName, String lastName) {

        //validateName(firstName, lastName);
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.balance = BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY); // Startbalance
    }
    public static Result<IUserEntity, ErrorWrapper> create(String firstName, String lastName) {

        var isNameValid = validateName(firstName);
        var isLastNameValid = validateName(lastName);
        if (isNameValid.isFailure() || isLastNameValid.isFailure()) {
            return Result.failure(
                    isNameValid.isFailure() ? isNameValid.getFailureData().orElse(ErrorWrapper.UNEXPECTED_INTERNAL_ERROR_OCCURED) :
                            isLastNameValid.getFailureData().orElse(ErrorWrapper.UNEXPECTED_INTERNAL_ERROR_OCCURED));
        }
        return Result.success(new UserEntity(firstName, lastName));
    }


    public ErrorResult<ErrorWrapper> deposit(BigDecimal amount) {
        var validationResult = validateAmount(amount);
        if (validationResult.isFailure()) {
            return validationResult;
        }
        this.balance = this.balance.add(amount);
        return ErrorResult.success();
    }

     public ErrorResult<ErrorWrapper> withdraw(BigDecimal amount) {
        var validationResult = validateAmount(amount);
        if (validationResult.isFailure()) {
            return validationResult;
        }
        if (balance.compareTo(amount) < 0) {
            return ErrorResult.failure(ErrorWrapper.USER_MODEL_INSUFFICIENT_BALANCE);
        }


        this.balance = this.balance.subtract(amount);
        return ErrorResult.success();
    }

    //helprer methods


    private ErrorResult<ErrorWrapper> validateAmount(BigDecimal amount) {
        if (amount == null) {
            return ErrorResult.failure(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_NULL);
        }

        if (amount.scale() > 2) {
            return ErrorResult.failure(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_DECIMAL_PLACES);
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ErrorResult.failure(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_NEGATIVE);
        }

        return ErrorResult.success();
    }

    private static ErrorResult<ErrorWrapper> validateName(String name) {
        if (name == null || name.isBlank()) {
            return ErrorResult.failure(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL);
        }
        if (name.length() > 12) {
            return ErrorResult.failure(ErrorWrapper.USER_MODEL_INVALID_NAME_LENGTH);
        }


        return ErrorResult.success();
    }




    public ErrorResult<ErrorWrapper> rename(String newFirstName, String newLastName) {

        var firstNameValidation = validateName(newFirstName);
        if (firstNameValidation.isFailure()) {
            return firstNameValidation;
        }

        var lastNameValidation = validateName(newLastName);
        if (lastNameValidation.isFailure()) {
            return lastNameValidation;
        }
        if (firstName.equals(newFirstName) && lastName.equals(newLastName)) {
            return ErrorResult.failure(ErrorWrapper.USER_MODEL_IDENTICAL_NAME);
        }

        this.firstName = newFirstName.trim();
        this.lastName = newLastName.trim();

        return ErrorResult.success();
    }


    // Getter
    public Long getId() { return id; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public BigDecimal getBalance() { return balance; }



}