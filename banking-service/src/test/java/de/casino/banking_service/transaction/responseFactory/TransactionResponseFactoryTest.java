package de.casino.banking_service.transaction.responseFactory;

import de.casino.banking_service.transaction.model.ITransactionEntity;
import de.casino.banking_service.transaction.response.transactionResponse.*;
import de.casino.banking_service.transaction.utility.Games;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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
        assertEquals(new BigDecimal("10.00"), r.getamount());
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
}