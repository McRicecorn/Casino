package de.casino.banking_service.transaction.model;

import de.casino.banking_service.transaction.utility.ErrorWrapper;
import de.casino.banking_service.transaction.utility.Games;
import de.casino.banking_service.user.model.IUserEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import de.casino.banking_service.user.model.UserEntity;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


// Tests 
class TransactionEntityTest {

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

    private ITransactionEntity transaction;

    private UserEntity createUser() {
        return (UserEntity) UserEntity.create("Max", "Mustermann")
                .getSuccessData()
                .get();
    }

    @Nested
    class CreateMethodTests{


        @Test
        public void create_validTransaction_shouldSucceed() {
            var user = createUser();
            var game = Games.ROULETTE;
            var amount = new BigDecimal("10.00");

            var result = TransactionEntity.create(amount, game, user);



            assertTrue(result.isSuccess());
            assertEquals(amount, result.getSuccessData().get().getAmount());
            assertEquals(game, result.getSuccessData().get().getInvoicingParty() );
            assertEquals(user, result.getSuccessData().get().getUser());
            assertEquals(user.getId(), result.getSuccessData().get().getUserId());

        }


        @Test
        public void create_nullAmount_shouldFail() {

            var user = createUser();
            var game = Games.ROULETTE;
            BigDecimal amount = null;

            var result = TransactionEntity.create(amount, game, user);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.AMOUNT_WAS_NULL, result.getFailureData().get());
        }

        @Test
        public void create_AmountZero_shouldFail() {
            var user = createUser();
            var game = Games.ROULETTE;
            var amount = new BigDecimal("0");

            var result = TransactionEntity.create(amount, game, user);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.AMOUNT_WAS_ZERO, result.getFailureData().get());
        }

        @Test
        public void create_TooManyDecimals_shouldFail(){
            var user = createUser();
            var game = Games.ROULETTE;
            var amount = new BigDecimal("10.000");

            var result = TransactionEntity.create(amount, game, user);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.AMOUNT_HAS_TOO_MANY_DECIMAL_PLACES,result.getFailureData().get());
        }

        @Test
        public void create_InvoicePartyNull_shouldFail(){
            var user = createUser();
            var amount = new BigDecimal("10.00");

            var result = TransactionEntity.create(amount, null, user);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.INVOICING_PARTY_DOES_NOT_EXIST, result.getFailureData().get());
        }




    }

    @Nested
    class UpdateMethodTests{

        @BeforeEach
        void setUp() {
            var oldUser = createUser();
            var oldAmount = new BigDecimal("10.00");
            var oldGame = Games.ROULETTE;
            transaction = TransactionEntity.create(oldAmount, oldGame, oldUser).getSuccessData().get();
        }

        @Test
        public void update_validTransaction_shouldSucceed() {

            var amount = new BigDecimal("20.00");
            var game = Games.SLOTS;

            var result = transaction.update(amount, game);

            assertTrue(result.isSuccess());
            assertEquals(amount, transaction.getAmount());
            assertEquals(game, transaction.getInvoicingParty());

        }

        @Test
        public void update_nullAmount_shouldFail() {
            var game = Games.SLOTS;
            var oldAmount = transaction.getAmount();
            var oldGame = transaction.getInvoicingParty();
            var result = transaction.update(null, game);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.AMOUNT_WAS_NULL, result.getFailureData().get());
            assertEquals(oldAmount, transaction.getAmount());
            assertEquals(oldGame, transaction.getInvoicingParty());
        }

        @Test
        public void update_AmountZero_shouldFail() {
            var game = Games.SLOTS;
            var amount = new BigDecimal("0");
            var oldAmount = transaction.getAmount();
            var oldGame = transaction.getInvoicingParty();

            var result = transaction.update(amount, game);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.AMOUNT_WAS_ZERO, result.getFailureData().get());
            assertEquals(oldAmount, transaction.getAmount());
            assertEquals(oldGame, transaction.getInvoicingParty());
        }

        @Test
        public void update_TooManyDecimals_shouldFail(){
            var game = Games.SLOTS;
            var amount = new BigDecimal("20.000");
            var oldAmount = transaction.getAmount();
            var oldGame = transaction.getInvoicingParty();

            var result = transaction.update(amount, game);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.AMOUNT_HAS_TOO_MANY_DECIMAL_PLACES,result.getFailureData().get());
            assertEquals(oldAmount, transaction.getAmount());
            assertEquals(oldGame, transaction.getInvoicingParty());
        }

        @Test
        public void update_InvoicePartyNull_shouldFail(){

            var amount = new BigDecimal("20.00");
            var oldAmount = transaction.getAmount();
            var oldGame = transaction.getInvoicingParty();
            var oldId = transaction.getTransactionId();
            var result = transaction.update(amount, null);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.INVOICING_PARTY_DOES_NOT_EXIST, result.getFailureData().get());
            assertEquals(oldAmount, transaction.getAmount());
            assertEquals(oldGame, transaction.getInvoicingParty());
            assertEquals(oldId, transaction.getTransactionId());
        }




    }




}