package de.casino.banking_service.transaction.responseFactory;

import de.casino.banking_service.common.Games;
import de.casino.banking_service.transaction.model.ITransactionEntity;
import de.casino.banking_service.transaction.response.transactionResponse.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionResponseFactoryTest {

    private TransactionResponseFactory factory;
    private ITransactionEntity transaction;

    @BeforeEach
    void setUp() {
        factory = new TransactionResponseFactory();

        transaction = mock(ITransactionEntity.class);

        when(transaction.getTransactionId()).thenReturn(1L);
        when(transaction.getUserId()).thenReturn(2L);
        when(transaction.getAmount()).thenReturn(new BigDecimal("10.00"));
        when(transaction.getInvoicingParty()).thenReturn(Games.ROULETTE);
    }

    @Test
    void createGetAll_shouldReturnCorrectResponse() {
        var response = factory.createGetAll(transaction);

        assertInstanceOf(GetAllTransactionResponse.class, response);

        GetAllTransactionResponse r = (GetAllTransactionResponse) response;

        assertEquals(1L, r.getId());
        assertEquals(2L, r.getUserId());
        assertEquals(new BigDecimal("10.00"), r.getAmount());
    }

    @Test
    void createGetByUser_shouldReturnCorrectResponse() {
        var response = factory.createGetByUser(transaction);

        assertInstanceOf(GetUserTransactionResponse.class, response);

        GetUserTransactionResponse r = (GetUserTransactionResponse) response;

        assertEquals(1L, r.getId());
        assertEquals(new BigDecimal("10.00"), r.getAmount());
    }

    @Test
    void createPost_shouldReturnCorrectResponse() {
        var response = factory.createPost(transaction);

        assertInstanceOf(PostTransactionResponse.class, response);

        PostTransactionResponse r = (PostTransactionResponse) response;

        assertEquals(1L, r.getId());
        assertEquals(2L, r.getUserId());
        assertEquals(new BigDecimal("10.00"), r.getAmount());
        assertEquals(Games.ROULETTE, r.getInvoicingParty());
    }

    @Test
    void createPut_shouldReturnCorrectResponse() {
        var response = factory.createPut(transaction);

        assertInstanceOf(PutTransactionResponse.class, response);

        PutTransactionResponse r = (PutTransactionResponse) response;

        assertEquals(1L, r.getId());
        assertEquals(2L, r.getUserId());
        assertEquals(new BigDecimal("10.00"), r.getAmount());
        assertEquals(Games.ROULETTE, r.getInvoicingParty());
    }

    @Test
    void createDelete_shouldReturnCorrectResponse() {
        var response = factory.createDelete(transaction);

        assertInstanceOf(DeleteTransactionResponse.class, response);

        DeleteTransactionResponse r = (DeleteTransactionResponse) response;

        assertEquals(2L, r.getUserId());
        assertEquals(new BigDecimal("10.00"), r.getAmount());
        assertEquals(Games.ROULETTE, r.getInvoicingParty());
    }



    @Test
    void createDeleteAll_shouldReturnCorrectResponse() {

        int number = 3;
        BigDecimal lost = new BigDecimal("10");
        BigDecimal gained = new BigDecimal("20");
        List<Games> games = List.of(Games.ROULETTE, Games.SLOTS);

        ITransactionResponse result =
                factory.createDeleteAll(number, lost, gained, games);

        assertNotNull(result);
        assertInstanceOf(DeleteAllTransactionsResponse.class, result);

        DeleteAllTransactionsResponse response = (DeleteAllTransactionsResponse) result;

        assertEquals(3, response.getNumberOfDeletedTransactions());
        assertEquals(lost, response.getTotalAmountLost());
        assertEquals(gained, response.getTotalAmountGained());
        assertEquals(games, response.getInvoicingPartys());
    }

    @Test
    void createDeleteAll_withEmptyValues_shouldStillWork() {

        ITransactionResponse result =
                factory.createDeleteAll(
                        0,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        List.of()
                );

        DeleteAllTransactionsResponse response = (DeleteAllTransactionsResponse) result;

        assertEquals(0, response.getNumberOfDeletedTransactions());
        assertEquals(BigDecimal.ZERO, response.getTotalAmountLost());
        assertEquals(BigDecimal.ZERO, response.getTotalAmountGained());
        assertTrue(response.getInvoicingPartys().isEmpty());
    }
}
