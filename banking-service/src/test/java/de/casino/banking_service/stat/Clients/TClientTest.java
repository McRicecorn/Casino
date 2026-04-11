package de.casino.banking_service.stat.Clients;

import de.casino.banking_service.stat.Responses.transactionResponses.GetAllTransactionsTClientResponse;
import de.casino.banking_service.stat.Utility.ErrorWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TClientTest {

    private RestTemplate restTemplate;
    private TClient tClient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        tClient = new TClient(restTemplate, "http://localhost:8080");
    }


    @Test
    void getAllTransactionsById_success_shouldReturnResult() {

        long userId = 1L;

        GetAllTransactionsTClientResponse[] responseArray =
                new GetAllTransactionsTClientResponse[] {
                        new GetAllTransactionsTClientResponse(
                                1L,
                                userId,
                                new BigDecimal("10"),
                                de.casino.banking_service.common.Games.ROULETTE
                        )
                };

        ResponseEntity<GetAllTransactionsTClientResponse[]> responseEntity =
                new ResponseEntity<>(responseArray, HttpStatus.OK);

        when(restTemplate.getForEntity(
                anyString(),
                eq(GetAllTransactionsTClientResponse[].class)
        )).thenReturn(responseEntity);

        var result = tClient.getAllTransactionsById(userId);

        assertTrue(result.isSuccess());

        var list = result.getSuccessData().get();
        assertEquals(1, ((java.util.Collection<?>) list).size());

        verify(restTemplate).getForEntity(
                contains(String.valueOf(userId)),
                eq(GetAllTransactionsTClientResponse[].class)
        );
    }

    @Test
    void getAllTransactionsById_notFound_shouldReturnUserError() {

        long userId = 1L;

        HttpClientErrorException exception =
                HttpClientErrorException.create(
                        HttpStatus.NOT_FOUND,
                        "Not Found",
                        null,
                        null,
                        null
                );

        when(restTemplate.getForEntity(
                anyString(),
                eq(GetAllTransactionsTClientResponse[].class)
        )).thenThrow(exception);

        var result = tClient.getAllTransactionsById(userId);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_WAS_NOT_FOUND,
                result.getFailureData().get());
    }

    @Test
    void getAllTransactionsById_otherError_shouldReturnExternalError() {

        long userId = 1L;

        HttpClientErrorException exception =
                HttpClientErrorException.create(
                        HttpStatus.BAD_REQUEST,
                        "Bad Request",
                        null,
                        null,
                        null
                );

        when(restTemplate.getForEntity(
                anyString(),
                eq(GetAllTransactionsTClientResponse[].class)
        )).thenThrow(exception);

        var result = tClient.getAllTransactionsById(userId);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.EXTERNAL_SERVICE_ERROR,
                result.getFailureData().get());
    }


    @Test
    void getAllTransactions_success_shouldReturnResult() {

        GetAllTransactionsTClientResponse[] responseArray =
                new GetAllTransactionsTClientResponse[] {
                        new GetAllTransactionsTClientResponse(
                                1L,
                                2L,
                                new BigDecimal("50"),
                                de.casino.banking_service.common.Games.SLOTS
                        )
                };

        ResponseEntity<GetAllTransactionsTClientResponse[]> responseEntity =
                new ResponseEntity<>(responseArray, HttpStatus.OK);

        when(restTemplate.getForEntity(
                eq(anyString()),
                eq(GetAllTransactionsTClientResponse[].class)
        )).thenReturn(responseEntity);

        var result = tClient.getAllTransactions();

        assertTrue(result.isSuccess());

        var list = result.getSuccessData().get();
        assertEquals(1, ((java.util.Collection<?>) list).size());
    }

    @Test
    void getAllTransactions_exception_shouldReturnExternalError() {

        when(restTemplate.getForEntity(
                anyString(),
                eq(GetAllTransactionsTClientResponse[].class)
        )).thenThrow(new RuntimeException("downstream error"));

        var result = tClient.getAllTransactions();

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.EXTERNAL_SERVICE_ERROR,
                result.getFailureData().get());
    }
}