package de.casino.banking_service.transaction.controller;

import de.casino.banking_service.common.Result;
import de.casino.banking_service.transaction.handler.ITransactionHandler;
import de.casino.banking_service.transaction.request.PostTransactionRequest;
import de.casino.banking_service.transaction.request.PutTransactionRequest;
import de.casino.banking_service.transaction.response.transactionResponse.ITransactionResponse;
import de.casino.banking_service.transaction.utility.ErrorWrapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    private TransactionController controller;
    private ITransactionHandler mockHandler;

    @BeforeEach
    public void setUp() {
        mockHandler = mock(ITransactionHandler.class);
        controller = new TransactionController(mockHandler);
    }

    @Test
    void getAllTransactions_shouldReturnList() {

        var list = List.of(mock(ITransactionResponse.class), mock(ITransactionResponse.class));

        when(mockHandler.getAllTransactions())
                .thenReturn(Result.success(list));

        var response = controller.getAllTransactions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());

        verify(mockHandler).getAllTransactions();
    }


    @Test
    void getTransactionsByUser_shouldReturnList() {

        Long userId = 1L;
        var list = List.of(mock(ITransactionResponse.class));

        when(mockHandler.getTransactionsByUserId(userId))
                .thenReturn(Result.success(list));

        var response = controller.getAllTransactionsByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());

        verify(mockHandler).getTransactionsByUserId(userId);
    }

    @Test
    void createTransaction_success_shouldReturnCreated() {

        Long userId = 1L;
        PostTransactionRequest request = mock(PostTransactionRequest.class);
        ITransactionResponse responseMock = mock(ITransactionResponse.class);

        when(mockHandler.createTransaction(request, userId))
                .thenReturn(Result.success(responseMock));

        var response = controller.createTransaction(userId, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseMock, response.getBody());

        verify(mockHandler).createTransaction(request, userId);
    }

    @Test
    void createTransaction_failure_shouldReturnError() {

        Long userId = 1L;
        PostTransactionRequest request = mock(PostTransactionRequest.class);
        ErrorWrapper error = mock(ErrorWrapper.class);

        when(mockHandler.createTransaction(request, userId))
                .thenReturn(Result.failure(error));

        when(error.getHttpStatus()).thenReturn(HttpStatus.BAD_REQUEST);
        when(error.getMessage()).thenReturn("Invalid transaction");

        var response = controller.createTransaction(userId, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid transaction", response.getBody());
    }


    @Test
    void updateTransaction_success_shouldReturnOk() {

        Long id = 1L;
        PutTransactionRequest request = mock(PutTransactionRequest.class);
        ITransactionResponse responseMock = mock(ITransactionResponse.class);

        when(mockHandler.updateTransaction(id, request))
                .thenReturn(Result.success(responseMock));

        var response = controller.updateTransaction(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseMock, response.getBody());

        verify(mockHandler).updateTransaction(id, request);
    }

    @Test
    void updateTransaction_failure_shouldReturnError() {

        Long id = 1L;
        PutTransactionRequest request = mock(PutTransactionRequest.class);
        ErrorWrapper error = mock(ErrorWrapper.class);

        when(mockHandler.updateTransaction(id, request))
                .thenReturn(Result.failure(error));

        when(error.getHttpStatus()).thenReturn(HttpStatus.NOT_FOUND);
        when(error.getMessage()).thenReturn("Transaction not found");

        var response = controller.updateTransaction(id, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Transaction not found", response.getBody());
    }

    @Test
    void deleteTransaction_success_shouldReturnOk() {

        Long id = 1L;
        ITransactionResponse responseMock = mock(ITransactionResponse.class);

        when(mockHandler.deleteTransaction(id))
                .thenReturn(Result.success(responseMock));

        var response = controller.deleteTransaction(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseMock, response.getBody());

        verify(mockHandler).deleteTransaction(id);
    }

    @Test
    void deleteTransaction_failure_shouldReturnError() {

        Long id = 1L;
        ErrorWrapper error = mock(ErrorWrapper.class);

        when(mockHandler.deleteTransaction(id))
                .thenReturn(Result.failure(error));

        when(error.getHttpStatus()).thenReturn(HttpStatus.NOT_FOUND);
        when(error.getMessage()).thenReturn("Transaction not found");

        var response = controller.deleteTransaction(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Transaction not found", response.getBody());
    }
    @Test
    void deleteByUserId_success_shouldReturnOk() {

        Long userId = 1L;
        ITransactionResponse responseMock = mock(ITransactionResponse.class);

        when(mockHandler.deleteTransactionsByUserId(userId))
                .thenReturn(Result.success(responseMock));

        var response = controller.deleteByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseMock, response.getBody());

        verify(mockHandler).deleteTransactionsByUserId(userId);
    }

    @Test
    void deleteByUserId_failure_shouldReturnError() {

        Long userId = 1L;
        ErrorWrapper error = mock(ErrorWrapper.class);

        when(mockHandler.deleteTransactionsByUserId(userId))
                .thenReturn(Result.failure(error));

        when(error.getHttpStatus()).thenReturn(HttpStatus.BAD_REQUEST);
        when(error.getMessage()).thenReturn("Delete failed");

        var response = controller.deleteByUserId(userId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Delete failed", response.getBody());

        verify(mockHandler).deleteTransactionsByUserId(userId);
    }

}