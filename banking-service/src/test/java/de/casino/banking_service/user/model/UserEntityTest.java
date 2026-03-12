package de.casino.banking_service.user.model;


import de.casino.banking_service.user.Utility.ErrorWrapper;
import de.casino.banking_service.user.exceptions.InvalidAmountException;
import de.casino.banking_service.user.exceptions.InvalidUserDataException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {
    //Create method tests
    @Test
    void create_validUser_shouldSucceed() {

        var result = UserEntity.create("Max", "Mustermann");

        assertTrue(result.isSuccess());
        assertEquals("Max", result.getSuccessData().get().getFirstName());
        assertEquals("Mustermann", result.getSuccessData().get().getLastName());
        assertEquals(BigDecimal.ZERO.setScale(2), result.getSuccessData().get().getBalance());


    }

    @Test
    void create_firstNameWith12Characters_shouldSucceed() {
        var result = UserEntity.create("Maximilian", "Mustermann");
        assertTrue(result.isSuccess());
        assertEquals("Maximilian", result.getSuccessData().get().getFirstName());
    }
    @Test
    void create_firstNameWithMoreThan12Characters_shouldFail() {
        var result = UserEntity.create("MaximilianXXX", "Mustermann");
        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_LENGTH, result.getFailureData().get());
    }

    @Test
    void create_lastNameWith12Characters_shouldSucceed() {
        var result = UserEntity.create("Max", "MustermannXX");
        assertTrue(result.isSuccess());
    }

    @Test
    void create_lastNameWithMoreThan12Characters_shouldFail() {
        var result = UserEntity.create("Max", "MustermannXYXX");
        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_LENGTH, result.getFailureData().get());
    }



    @Test
    void create_nullFirstName_shouldFail() {
        var result = UserEntity.create(null, "Mustermann");
        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL, result.getFailureData().get());
    }

    @Test
    void create_nullLastName_shouldFail() {
        var result = UserEntity.create("Max", null);
        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL, result.getFailureData().get());
    }

    @Test
    void create_blankFirstName_shouldFail() {
        var result = UserEntity.create("", "Mustermann");
        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL, result.getFailureData().get());
    }

    @Test
    void create_blankLastName_shouldFail() {
        var result = UserEntity.create("Max", "");
        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL, result.getFailureData().get());

    }

    @Test
    void create_whitespaceFirstName_shouldFail() {
        var result = UserEntity.create("   ", "Mustermann");
        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL, result.getFailureData().get());
    }
    @Test
    void create_whitespaceLastName_shouldFail() {
        var result = UserEntity.create("Max", "   ");
        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL, result.getFailureData().get());
    }

    @Test
    void create_firstNameWithSpaces_shouldTrim() {
        var result = UserEntity.create("  Max  ", "Mustermann");
        assertTrue(result.isSuccess());
        assertEquals("Max", result.getSuccessData().get().getFirstName());

    }

    @Test
    void create_lastNameWithSpaces_shouldTrim() {
        var result = UserEntity.create("Max", "  Muster  ");
        assertTrue(result.isSuccess());
        assertEquals("Muster", result.getSuccessData().get().getLastName());
    }


    // deposit

    @Test
    void deposit_validAmount_shouldSucceed() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.deposit(new BigDecimal("100.00"));

        assertTrue(result.isSuccess());
        assertEquals(new BigDecimal("100.00"), user.getBalance());
    }

    @Test
    void deposit_amountZero_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.deposit(BigDecimal.ZERO);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT, result.getFailureData().get());
    }

    @Test
    void deposit_amountWithLessThanTwoDecimalPlaces_shouldSucceed() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.deposit(new BigDecimal("10.1"));

        assertTrue(result.isSuccess());
        assertEquals(new BigDecimal("10.10"), user.getBalance());
    }

    @Test
    void deposit_nullAmount_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.deposit(null);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_NULL, result.getFailureData().get());
    }

    @Test
    void deposit_negativeAmount_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.deposit(new BigDecimal("-10.00"));

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_NEGATIVE, result.getFailureData().get());
    }

    @Test
    void deposit_tooManyDecimalPlaces_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.deposit(new BigDecimal("10.123"));

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_DECIMAL_PLACES, result.getFailureData().get());
    }


    @Test
    void deposit_integerAmount_shouldSucceed() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.deposit(new BigDecimal("50"));

        assertTrue(result.isSuccess());
        assertEquals(new BigDecimal("50.00"), user.getBalance());
    }

    //Withdraw

    @Test
    void withdraw_validAmount_shouldSucceed() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();
        user.deposit(new BigDecimal("100.00"));

        var result = user.withdraw(new BigDecimal("30.00"));

        assertTrue(result.isSuccess());
        assertEquals(new BigDecimal("70.00"), user.getBalance());
    }

    @Test
    void withdraw_amountExceedingBalance_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();
        user.deposit(new BigDecimal("50.00"));

        var result = user.withdraw(new BigDecimal("60.00"));

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INSUFFICIENT_BALANCE, result.getFailureData().get());
    }
    @Test
    void withdraw_amountWithLessThanTwoDecimalPlaces_shouldSucceed() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();
        user.deposit(new BigDecimal("100.00"));

        var result = user.withdraw(new BigDecimal("10.1"));

        assertTrue(result.isSuccess());
        assertEquals(new BigDecimal("89.90"), user.getBalance());
    }

    @Test
    void withdraw_nullAmount_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();
        user.deposit(new BigDecimal("100.00"));

        var result = user.withdraw(null);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_NULL, result.getFailureData().get());
    }

    @Test
    void withdraw_negativeAmount_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();
        user.deposit(new BigDecimal("100.00"));

        var result = user.withdraw(new BigDecimal("-10.00"));

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_NEGATIVE, result.getFailureData().get());
    }

    @Test
    void withdraw_tooManyDecimalPlaces_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();
        user.deposit(new BigDecimal("100.00"));

        var result = user.withdraw(new BigDecimal("10.123"));

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_DECIMAL_PLACES, result.getFailureData().get());
    }

    @Test
    void withdraw_zeroAmount_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();
        user.deposit(new BigDecimal("100.00"));

        var result = user.withdraw(BigDecimal.ZERO);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT, result.getFailureData().get());
    }

    @Test
    void withdraw_exactBalance_shouldSucceed() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();
        user.deposit(new BigDecimal("100.00"));

        var result = user.withdraw(new BigDecimal("100.00"));

        assertTrue(result.isSuccess());
        assertEquals(BigDecimal.ZERO.setScale(2), user.getBalance());
    }

    //rename

    @Test
    void rename_validNames_shouldSucceed() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.rename("Jane", "Smith");

        assertTrue(result.isSuccess());
        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
    }

    @Test
    void rename_nullFirstName_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.rename(null, "Smith");

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL, result.getFailureData().get());

    }
    @Test
    void rename_nullLastName_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.rename("Jane", null);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL, result.getFailureData().get());

    }

    @Test
    void rename_blankFirstName_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.rename("", "Smith");

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL, result.getFailureData().get());

    }

    @Test
    void rename_blankLastName_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.rename("Jane", "");

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL, result.getFailureData().get());

    }

    @Test
    void rename_whitespaceFirstName_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.rename("   ", "Smith");

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL, result.getFailureData().get());

    }

    @Test
    void rename_whitespaceLastName_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.rename("Jane", "   ");

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL, result.getFailureData().get());

    }

    @Test
    void rename_firstNameWith12Characters_shouldSucceed() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.rename("MaximilianXX", "Smith");

        assertTrue(result.isSuccess());

    }

    @Test
    void rename_lastNameWith12Characters_shouldSucceed() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.rename("Jane", "MaximillianX");

        assertTrue(result.isSuccess());

    }

    @Test
    void rename_firstNameWithMoreThan12Characters_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.rename("MaximilianXXX", "Smith");

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_LENGTH, result.getFailureData().get());
    }

    @Test
    void rename_lastNameWithMoreThan12Characters_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.rename("Jane", "MaximillianXXX");

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_LENGTH, result.getFailureData().get());
    }

    @Test
    void rename_identicalFirstAndLastName_shouldFail() {
        var user = UserEntity.create("John", "Doe").getSuccessData().get();

        var result = user.rename("John", "Doe");

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_MODEL_IDENTICAL_NAME, result.getFailureData().get());
    }

    //validateAmount






}