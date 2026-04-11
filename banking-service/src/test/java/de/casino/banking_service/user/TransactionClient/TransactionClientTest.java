package de.casino.banking_service.user.TransactionClient;

import de.casino.banking_service.common.Result;
import de.casino.banking_service.user.Response.TransactionResponse.DeleteAllTransactionsClient;
import de.casino.banking_service.user.Utility.ErrorWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TransactionClient transactionClient;


    @Test
    void deleteTransactionsByUserId_success_shouldReturnSuccess() {

        long userId = 1L;

        DeleteAllTransactionsClient body =
                new DeleteAllTransactionsClient(
                        3,
                        new BigDecimal("10"),
                        new BigDecimal("20"),
                        List.of()
                );

        ResponseEntity<DeleteAllTransactionsClient> response =
                new ResponseEntity<>(body, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.DELETE),
                isNull(),
                eq(DeleteAllTransactionsClient.class)
        )).thenReturn(response);

        var result = transactionClient.deleteTransactionsByUserId(userId);

        assertTrue(result.isSuccess());
        assertEquals(3, result.getSuccessData().get().getNumberOfDeletedTransactions());

        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.DELETE),
                isNull(),
                eq(DeleteAllTransactionsClient.class)
        );
    }


    @Test
    void deleteTransactionsByUserId_non2xx_shouldReturnExternalServiceError() {

        ResponseEntity<DeleteAllTransactionsClient> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.DELETE),
                isNull(),
                eq(DeleteAllTransactionsClient.class)
        )).thenReturn(response);

        var result = transactionClient.deleteTransactionsByUserId(1L);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.EXTERNAL_SERVICE_ERROR, result.getFailureData().get());
    }


    @Test
    void deleteTransactionsByUserId_nullBody_shouldReturnInvalidAmountError() {

        ResponseEntity<DeleteAllTransactionsClient> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.DELETE),
                isNull(),
                eq(DeleteAllTransactionsClient.class)
        )).thenReturn(response);

        var result = transactionClient.deleteTransactionsByUserId(1L);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.EXTERNAL_SERVICE_ERROR, result.getFailureData().get());
    }

    @Test
    void deleteTransactionsByUserId_exception_shouldReturnExternalServiceError() {

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.DELETE),
                isNull(),
                eq(DeleteAllTransactionsClient.class)
        )).thenThrow(new RuntimeException("boom"));

        var result = transactionClient.deleteTransactionsByUserId(1L);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.EXTERNAL_SERVICE_ERROR, result.getFailureData().get());
    }
}