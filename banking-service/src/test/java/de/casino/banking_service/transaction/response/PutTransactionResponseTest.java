package de.casino.banking_service.transaction.response;

import de.casino.banking_service.transaction.response.transactionResponse.PutTransactionResponse;
import de.casino.banking_service.transaction.utility.Games;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PutTransactionResponseTest {

    @Test
    void constructor_validInput_shouldSetFieldsCorrectly() {
        long id = 1L;
        long userId = 2L;
        BigDecimal amount = new BigDecimal("75.00");
        Games game = Games.SLOTS;

        PutTransactionResponse response =
                new PutTransactionResponse(id, userId, amount, game);

        assertEquals(id, response.getId());
        assertEquals(userId, response.getUserId());
        assertEquals(amount, response.getAmount());
        assertEquals(game, response.getInvoicingParty());
    }

    @Test
    void constructor_nullValues_shouldAllowNull() {
        long id = 1L;
        long userId = 2L;

        PutTransactionResponse response =
                new PutTransactionResponse(id, userId, null, null);

        assertEquals(id, response.getId());
        assertEquals(userId, response.getUserId());
        assertNull(response.getAmount());
        assertNull(response.getInvoicingParty());
    }
}