package de.casino.banking_service.transaction.response;

import de.casino.banking_service.common.Games;
import de.casino.banking_service.transaction.response.transactionResponse.PostTransactionResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PostTransactionResponseTest {

    @Test
    void constructor_validInput_shouldSetFieldsCorrectly() {
        long id = 1L;
        long userId = 2L;
        BigDecimal amount = new BigDecimal("50.00");
        Games game = Games.ROULETTE;

        PostTransactionResponse response =
                new PostTransactionResponse(id, userId, amount, game);

        assertEquals(id, response.getId());
        assertEquals(userId, response.getUserId());
        assertEquals(amount, response.getAmount());
        assertEquals(game, response.getInvoicingParty());
    }

    @Test
    void constructor_nullValues_shouldAllowNull() {
        long id = 1L;
        long userId = 2L;

        PostTransactionResponse response =
                new PostTransactionResponse(id, userId, null, null);

        assertEquals(id, response.getId());
        assertEquals(userId, response.getUserId());
        assertNull(response.getAmount());
        assertNull(response.getInvoicingParty());
    }
}