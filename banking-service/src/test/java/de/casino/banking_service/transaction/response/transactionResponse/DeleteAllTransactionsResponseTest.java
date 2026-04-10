package de.casino.banking_service.transaction.response.transactionResponse;

import de.casino.banking_service.common.Games;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteAllTransactionsResponseTest {

    @Test
    void constructorAndGetters_shouldSetAllFieldsCorrectly() {

        int number = 5;
        BigDecimal lost = new BigDecimal("10.50");
        BigDecimal gained = new BigDecimal("25.75");
        List<Games> games = List.of(Games.ROULETTE, Games.SLOTS);

        DeleteAllTransactionsResponse response =
                new DeleteAllTransactionsResponse(number, lost, gained, games);

        assertEquals(5, response.getNumberOfDeletedTransactions());
        assertEquals(lost, response.getTotalAmountLost());
        assertEquals(gained, response.getTotalAmountGained());
        assertEquals(games, response.getInvoicingPartys());
    }

    @Test
    void shouldHandleEmptyGameList() {

        DeleteAllTransactionsResponse response =
                new DeleteAllTransactionsResponse(
                        0,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        List.of()
                );

        assertEquals(0, response.getNumberOfDeletedTransactions());
        assertEquals(BigDecimal.ZERO, response.getTotalAmountLost());
        assertEquals(BigDecimal.ZERO, response.getTotalAmountGained());
        assertTrue(response.getInvoicingPartys().isEmpty());
    }

    @Test
    void shouldAllowNullValues() {

        DeleteAllTransactionsResponse response =
                new DeleteAllTransactionsResponse(
                        1,
                        null,
                        null,
                        null
                );

        assertNull(response.getTotalAmountLost());
        assertNull(response.getTotalAmountGained());
        assertNull(response.getInvoicingPartys());
    }
}