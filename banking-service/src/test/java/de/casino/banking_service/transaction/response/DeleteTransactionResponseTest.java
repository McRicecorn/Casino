package de.casino.banking_service.transaction.response;

import de.casino.banking_service.transaction.response.transactionResponse.DeleteTransactionResponse;
import de.casino.banking_service.transaction.utility.Games;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DeleteTransactionResponseTest {

    @Test
    void constructor_validInput_shouldSetFieldsCorrectly() {
        long userId = 1L;
        BigDecimal amount = new BigDecimal("100.00");
        Games game = Games.ROULETTE;

        DeleteTransactionResponse response =
                new DeleteTransactionResponse(userId, amount, game);

        assertEquals(userId, response.getUserId());
        assertEquals(amount, response.getAmount());
        assertEquals(game, response.getInvoicingParty());
    }

    @Test
    void constructor_nullValues_shouldAllowNull() {
        long userId = 1L;

        DeleteTransactionResponse response =
                new DeleteTransactionResponse(userId, null, null);

        assertEquals(userId, response.getUserId());
        assertNull(response.getAmount());
        assertNull(response.getInvoicingParty());
    }
}