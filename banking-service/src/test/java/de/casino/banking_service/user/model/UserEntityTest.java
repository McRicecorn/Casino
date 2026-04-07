package de.casino.banking_service.user.model;


import de.casino.banking_service.user.Utility.ErrorWrapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    private static Random rng;

    private static double randomPositive;
    private static double randomNegative;

    private static double heighestPositiveFraction;
    private static double lowestPositiveFraction;
    private static double zero;
    private static double heighestNegativeFraction;
    private static double lowestNegativeFraction;

    @BeforeAll
    public static void setUpAll() {
        rng = new Random();

        heighestPositiveFraction = Double.MAX_VALUE;
        lowestPositiveFraction = Double.MIN_VALUE;
        zero = 0.0d;
        heighestNegativeFraction = -Double.MIN_VALUE;
        lowestNegativeFraction = -Double.MAX_VALUE;
    }


    private IUserEntity user;

    @BeforeEach
    void setUp() {
         user = UserEntity.create("John", "Doe")
                .getSuccessData()
                .orElseThrow();
    }

    //Create method tests
    @Nested
    class CreateMethodTests {
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

        @Test
        void create_bothNamesInvalid_shouldReturnFirstNameError() {
            var result = UserEntity.create("", "");

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL,
                    result.getFailureData().get());
        }

    }




    @Nested
    class DepositTests {
        @Test
        void deposit_validAmount_shouldSucceed() {

            var result = user.deposit(new BigDecimal("600.00"));

            assertTrue(result.isSuccess());
            assertEquals(new BigDecimal("600.00"), user.getBalance());
        }

        @Test
        void deposit_amountZero_shouldFail() {

            var result = user.deposit(BigDecimal.ZERO);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT, result.getFailureData().get());
        }

        @Test
        void deposit_amountWithLessThanTwoDecimalPlaces_shouldSucceed() {

            var result = user.deposit(new BigDecimal("10.1"));

            assertTrue(result.isSuccess());
            assertEquals(new BigDecimal("10.10"), user.getBalance());
        }

        @Test
        void deposit_nullAmount_shouldFail() {

            var result = user.deposit(null);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_NULL, result.getFailureData().get());
        }

        @Test
        void deposit_negativeAmount_shouldFail() {

            var result = user.deposit(new BigDecimal("-10.00"));

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_NEGATIVE, result.getFailureData().get());
        }

        @Test
        void deposit_tooManyDecimalPlaces_shouldFail() {

            var result = user.deposit(new BigDecimal("10.123"));

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_DECIMAL_PLACES, result.getFailureData().get());
        }


        @Test
        void deposit_integerAmount_shouldSucceed() {

            var result = user.deposit(new BigDecimal("50"));

            assertTrue(result.isSuccess());
            assertEquals(new BigDecimal("50.00"), user.getBalance());
        }
    }

    @Nested
    class WithdrawTests {
        @Test
        void withdraw_validAmount_shouldSucceed() {
            user.deposit(new BigDecimal("100.00"));

            var result = user.withdraw(new BigDecimal("30.00"));

            assertTrue(result.isSuccess());
            assertEquals(new BigDecimal("70.00"), user.getBalance());
        }

        @Test
        void withdraw_amountExceedingBalance_shouldFail() {
            user.deposit(new BigDecimal("50.00"));

            var result = user.withdraw(new BigDecimal("60.00"));

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_INSUFFICIENT_BALANCE, result.getFailureData().get());
        }

        @Test
        void withdraw_amountWithLessThanTwoDecimalPlaces_shouldSucceed() {
            user.deposit(new BigDecimal("100.00"));

            var result = user.withdraw(new BigDecimal("10.1"));

            assertTrue(result.isSuccess());
            assertEquals(new BigDecimal("89.90"), user.getBalance());
        }

        @Test
        void withdraw_nullAmount_shouldFail() {
            user.deposit(new BigDecimal("100.00"));

            var result = user.withdraw(null);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_NULL, result.getFailureData().get());
        }

        @Test
        void withdraw_negativeAmount_shouldFail() {
            user.deposit(new BigDecimal("100.00"));

            var result = user.withdraw(new BigDecimal("-10.00"));

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_NEGATIVE, result.getFailureData().get());
        }

        @Test
        void withdraw_tooManyDecimalPlaces_shouldFail() {
            user.deposit(new BigDecimal("100.00"));

            var result = user.withdraw(new BigDecimal("10.123"));

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_DECIMAL_PLACES, result.getFailureData().get());
        }

        @Test
        void withdraw_zeroAmount_shouldFail() {
            user.deposit(new BigDecimal("100.00"));

            var result = user.withdraw(BigDecimal.ZERO);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT, result.getFailureData().get());
        }

        @Test
        void withdraw_exactBalance_shouldSucceed() {
            user.deposit(new BigDecimal("100.00"));

            var result = user.withdraw(new BigDecimal("100.00"));

            assertTrue(result.isSuccess());
            assertEquals(BigDecimal.ZERO.setScale(2), user.getBalance());
        }
    }


    @Nested
    class RenameTests {
        @Test
        void rename_validNames_shouldSucceed() {

            var result = user.rename("Jane", "Smith");

            assertTrue(result.isSuccess());
            assertEquals("Jane", user.getFirstName());
            assertEquals("Smith", user.getLastName());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   "})
        void rename_invalidFirstName_shouldFail(String invalidFirstName) {

            var result = user.rename(invalidFirstName, "Smith");

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL, result.getFailureData().get());

        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   "})
        void rename_invalidLastName_shouldFail(String invalidLastName) {

            var result = user.rename("Jane", invalidLastName);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL, result.getFailureData().get());

        }


        @ParameterizedTest
        @ValueSource(strings = {"MaximilianXXx", "ABCDEFGHIJKLM"})
        void rename_firstNameWithMoreThan12Characters_shouldFail(String invalidFirstName) {

            var result1 = user.rename(invalidFirstName, "Smith");

            assertTrue(result1.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_LENGTH, result1.getFailureData().get());


            var result2 = user.rename("Jane", invalidFirstName);
            assertTrue(result2.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_LENGTH, result2.getFailureData().get());

        }


        @Test
        void rename_firstNameWith12Characters_shouldSucceed() {

            var result = user.rename("MaximilianXX", "Smith");

            assertTrue(result.isSuccess());

        }

        @Test
        void rename_lastNameWith12Characters_shouldSucceed() {

            var result = user.rename("Jane", "MaximillianX");

            assertTrue(result.isSuccess());

        }


        @Test
        void rename_identicalFirstAndLastName_shouldFail() {

            var result = user.rename("John", "Doe");

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_IDENTICAL_NAME, result.getFailureData().get());
        }
    }

    @Test
    void getUserId(){
       var userId = user.getId();
         assertNull(userId);
    }
    @Test
    void constructor_shouldCreateInstance() {
        assertNotNull(new UserEntity());
    }





}