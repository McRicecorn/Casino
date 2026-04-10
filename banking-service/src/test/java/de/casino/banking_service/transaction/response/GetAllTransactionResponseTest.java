package de.casino.banking_service.transaction.response;

import static org.junit.jupiter.api.Assertions.*;

import de.casino.banking_service.common.Games;
import de.casino.banking_service.transaction.response.transactionResponse.GetAllTransactionResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class GetAllTransactionResponseTest {

    @Test
    void constructor_validInput_shouldSetFieldsCorrectly() {
        long id = 1L;
        long userId = 2L;
        BigDecimal amount = new BigDecimal("10.00");
        Games invoicingParty = Games.ROULETTE;

        GetAllTransactionResponse response =
                new GetAllTransactionResponse(id, userId, amount, invoicingParty);

        assertEquals(id, response.getId());
        assertEquals(userId, response.getUserId());
        assertEquals(amount, response.getAmount());
    }

    @Test
    void constructor_nullAmount_shouldAllowNull() {
        long id = 1L;
        long userId = 2L;
        Games invoicingParty = Games.ROULETTE;

        GetAllTransactionResponse response =
                new GetAllTransactionResponse(id, userId, null, invoicingParty);

        assertEquals(id, response.getId());
        assertEquals(userId, response.getUserId());
        assertEquals(invoicingParty, response.getInvoicingParty());
        assertNull(response.getAmount());
    }
}