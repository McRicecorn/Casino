package de.casino.banking_service.transaction.response;

import de.casino.banking_service.transaction.response.transactionResponse.GetUserTransactionResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class GetUserTransactionResponseTest {

    @Test
    void constructor_validInput_shouldSetFieldsCorrectly() {
        long id = 1L;
        BigDecimal amount = new BigDecimal("20.00");

        GetUserTransactionResponse response =
                new GetUserTransactionResponse(id, amount);

        assertEquals(id, response.getId());
        assertEquals(amount, response.getamount());
    }

    @Test
    void constructor_nullAmount_shouldAllowNull() {
        long id = 1L;

        GetUserTransactionResponse response =
                new GetUserTransactionResponse(id, null);

        assertEquals(id, response.getId());
        assertNull(response.getamount());
    }
}