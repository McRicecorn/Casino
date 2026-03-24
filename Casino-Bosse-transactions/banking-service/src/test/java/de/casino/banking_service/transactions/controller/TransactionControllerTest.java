package de.casino.banking_service.transactions.controller;

import de.casino.banking_service.transaction.controller.TransactionController;
import de.casino.banking_service.transaction.handler.ITransactionHandler;
import de.casino.banking_service.transaction.request.TransactionRequest;
import de.casino.banking_service.transaction.response.TransactionResponse;
import de.casino.banking_service.transaction.utility.Result;
import de.casino.banking_service.user.handler.UserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    private TransactionController controller;
    private ITransactionHandler mockTransactionHandler;
    private UserHandler mockuserHandler;

    @BeforeEach
    public void setUp(){

        mockTransactionHandler = mock(ITransactionHandler.class);
        mockuserHandler = mock(UserHandler.class);
        controller = new TransactionController(mockTransactionHandler,mockuserHandler);
    }
/*
    @Test
    @DisplayName("finds a Transaction by UserId and retruns a valid response")
    public void  findTransactionbyUserId(){
        Long userId = 1L;
        TransactionResponse mockResponse = mock(TransactionResponse.class);

        when(mockTransactionHandler.findByUserId(userId)).thenReturn(Result.success(mockResponse));

        ResponseEntity <?> response = controller.getTransactionsbyUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(mockTransactionHandler,times(1)).findByUserId(userId);
    }
*/
    @Test
    @DisplayName("create a Transaction for a valid request")
    public void createTransactionValidRequest(){
        Long userId = 1L;
        TransactionRequest mockRequest = mock(TransactionRequest.class);
        TransactionResponse mockResponse = mock(TransactionResponse.class);

        when(mockTransactionHandler.createTransactionEntity(mockRequest, userId)).thenReturn(Result.success(mockResponse));

        ResponseEntity<?> response = controller.createTransactionEntity(mockRequest, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRequest, response.getBody());
        verify(mockTransactionHandler, times(1)).createTransactionEntity(mockRequest,userId);
    }
}
