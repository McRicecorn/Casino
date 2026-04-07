package de.casino.banking_service.transaction.request;

import de.casino.banking_service.transaction.utility.Games;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PostTransactionRequestTest {

    @Test
    void constructor_validInput_shouldSetFieldsCorrectly() {

        BigDecimal amount = new BigDecimal("10.00");
        Games game = Games.ROULETTE;

        PostTransactionRequest request =
                new PostTransactionRequest(amount, game);

        assertEquals(amount, request.getAmount());
        assertEquals(game, request.getInvoicingParty());
    }

    @Test
    void constructor_nullValues_shouldSetFieldsToNull() {

        PostTransactionRequest request =
                new PostTransactionRequest(null, null);

        assertNull(request.getAmount());
        assertNull(request.getInvoicingParty());
    }
}