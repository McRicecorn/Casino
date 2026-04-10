package de.casino.banking_service.transaction.request;

import de.casino.banking_service.common.Games;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


class PutTransactionRequestTest {

    @Test
    void constructor_validInput_shouldSetFieldsCorrectly() {

        BigDecimal amount = new BigDecimal("20.00");
        Games game = Games.SLOTS;
        long userId = 1L;

        PutTransactionRequest request =
                new PutTransactionRequest(amount, game, userId);

        assertEquals(amount, request.getAmount());
        assertEquals(game, request.getInvoicingParty());
        assertEquals(userId, request.getUserId());
    }

    @Test
    void constructor_nullValues_shouldSetFieldsCorrectly() {

        long userId = 1L;

        PutTransactionRequest request =
                new PutTransactionRequest(null, null, userId);

        assertNull(request.getAmount());
        assertNull(request.getInvoicingParty());
        assertEquals(userId, request.getUserId());
    }
}

