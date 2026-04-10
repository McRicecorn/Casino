package de.casino.banking_service.user.Response.TransactionResponse;

import static org.junit.jupiter.api.Assertions.*;

import de.casino.banking_service.common.Games;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;


class DeleteAllTransactionsClientTest {

    @Test
    void constructor_shouldSetAllFieldsCorrectly() {

        // ARRANGE
        int numberOfDeletedTransactions = 5;
        BigDecimal totalLost = new BigDecimal("100.50");
        BigDecimal totalGained = new BigDecimal("250.00");
        List<Games> games = List.of(Games.ROULETTE, Games.SLOTS);

        // ACT
        DeleteAllTransactionsClient response =
                new DeleteAllTransactionsClient(
                        numberOfDeletedTransactions,
                        totalLost,
                        totalGained,
                        games
                );

        // ASSERT
        assertEquals(5, response.getNumberOfDeletedTransactions());
        assertEquals(new BigDecimal("100.50"), response.getTotalAmountLost());
        assertEquals(new BigDecimal("250.00"), response.getTotalAmountGained());
        assertEquals(games, response.getInvoicingPartys());
    }

    @Test
    void getters_shouldReturnSameValues() {

        // ARRANGE
        List<Games> games = List.of(Games.ROULETTE);

        DeleteAllTransactionsClient response =
                new DeleteAllTransactionsClient(
                        1,
                        new BigDecimal("10"),
                        new BigDecimal("20"),
                        games
                );

        // ASSERT
        assertEquals(1, response.getNumberOfDeletedTransactions());
        assertEquals(new BigDecimal("10"), response.getTotalAmountLost());
        assertEquals(new BigDecimal("20"), response.getTotalAmountGained());
        assertEquals(games, response.getInvoicingPartys());
    }

    @Test
    void shouldHandleEmptyGameList() {

        // ACT
        DeleteAllTransactionsClient response =
                new DeleteAllTransactionsClient(
                        0,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        List.of()
                );

        // ASSERT
        assertEquals(0, response.getNumberOfDeletedTransactions());
        assertTrue(response.getInvoicingPartys().isEmpty());
    }
}