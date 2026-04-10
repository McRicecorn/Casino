package de.casino.banking_service.transaction.modelFactory;

import de.casino.banking_service.common.Games;
import de.casino.banking_service.common.Result;
import de.casino.banking_service.transaction.model.ITransactionEntity;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionEntityFactoryTest {

    private TransactionEntityFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TransactionEntityFactory();
    }

    @Test
    void create_validInput_shouldSucceed() {

        BigDecimal amount = new BigDecimal("10.00");
        Games game = Games.ROULETTE;
        long userId = 1L;

        Result<ITransactionEntity, ErrorWrapper> result =
                factory.create(amount, game, userId);

        assertTrue(result.isSuccess());

        ITransactionEntity transaction = result.getSuccessData().get();

        assertEquals(amount, transaction.getAmount());
        assertEquals(game, transaction.getInvoicingParty());
        assertEquals(userId, transaction.getUserId());
    }

    @Test
    void create_nullAmount_shouldFail() {

        var result = factory.create(null, Games.ROULETTE, 1L);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.AMOUNT_WAS_NULL,
                result.getFailureData().get());
    }

    @Test
    void create_zeroAmount_shouldFail() {

        var result = factory.create(new BigDecimal("0"), Games.ROULETTE, 1L);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.AMOUNT_WAS_ZERO,
                result.getFailureData().get());
    }

    @Test
    void create_nullInvoicingParty_shouldFail() {

        var result = factory.create(new BigDecimal("10.00"), null, 1L);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.INVOICING_PARTY_DOES_NOT_EXIST,
                result.getFailureData().get());
    }
}