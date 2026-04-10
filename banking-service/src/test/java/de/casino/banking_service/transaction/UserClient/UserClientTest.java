package de.casino.banking_service.transaction.UserClient;

import de.casino.banking_service.transaction.response.userResponse.GetUserClientResponse;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserClient userClient;


    @Test
    void getUserById_success_shouldReturnUser() {

        Long userId = 1L;

        GetUserClientResponse userResponse = mock(GetUserClientResponse.class);

        ResponseEntity<GetUserClientResponse> responseEntity =
                new ResponseEntity<>(userResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(
                anyString(),
                eq(GetUserClientResponse.class)
        )).thenReturn(responseEntity);

        var result = userClient.getUserById(userId);

        assertTrue(result.isSuccess());
        assertEquals(userResponse, result.getSuccessData().get());
    }

    @Test
    void getUserById_notFound_shouldReturnFailure() {

        Long userId = 1L;

        HttpClientErrorException exception =
                mock(HttpClientErrorException.class);

        when(exception.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);

        when(restTemplate.getForEntity(anyString(), eq(GetUserClientResponse.class)))
                .thenThrow(exception);

        var result = userClient.getUserById(userId);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_WAS_NOT_FOUND,
                result.getFailureData().get());
    }

    @Test
    void getUserById_otherError_shouldReturnExternalError() {

        HttpClientErrorException exception =
                mock(HttpClientErrorException.class);

        when(exception.getStatusCode())
                .thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.getForEntity(anyString(), eq(GetUserClientResponse.class)))
                .thenThrow(exception);

        var result = userClient.getUserById(1L);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.EXTERNAL_SERVICE_ERROR,
                result.getFailureData().get());
    }
    @Test
    void deposit_success_shouldReturnSuccess() {

        Long userId = 1L;
        BigDecimal amount = new BigDecimal("10.50");

        when(restTemplate.postForEntity(anyString(), isNull(), eq(Object.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        var result = userClient.deposit(userId, amount);

        assertTrue(result.isSuccess());

        verify(restTemplate).postForEntity(
                contains("/1/deposit/10/50"),
                isNull(),
                eq(Object.class)
        );
    }
    @Test
    void deposit_withoutDecimals_shouldReturnSuccess() {

        Long userId = 1L;
        BigDecimal amount = new BigDecimal("10");

        when(restTemplate.postForEntity(anyString(), isNull(), eq(Object.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        var result = userClient.deposit(userId, amount);

        assertTrue(result.isSuccess());

        verify(restTemplate).postForEntity(
                contains("/1/deposit/10/00"),
                isNull(),
                eq(Object.class)
        );
    }

    @Test
    void deposit_failure_shouldReturnError() {

        when(restTemplate.postForEntity(anyString(), isNull(), eq(Object.class)))
                .thenThrow(new RuntimeException("error"));

        var result = userClient.deposit(1L, new BigDecimal("10.00"));

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_SERVICE_BAD_RESPONSE,
                result.getFailureData().get());
    }



    @Test
    void withdraw_success_shouldReturnSuccess() {

        Long userId = 1L;
        BigDecimal amount = new BigDecimal("5.25");

        when(restTemplate.postForEntity(anyString(), isNull(), eq(Object.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        var result = userClient.withdraw(userId, amount);

        assertTrue(result.isSuccess());

        verify(restTemplate).postForEntity(
                contains("/1/withdraw/5/25"),
                isNull(),
                eq(Object.class)
        );
    }

    @Test
    void withdraw_withoutDecimals_shouldReturnSuccess() {

        Long userId = 1L;
        BigDecimal amount = new BigDecimal("5");

        when(restTemplate.postForEntity(anyString(), isNull(), eq(Object.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        var result = userClient.withdraw(userId, amount);

        assertTrue(result.isSuccess());

        verify(restTemplate).postForEntity(
                contains("/1/withdraw/5/00"),
                isNull(),
                eq(Object.class)
        );
    }

    @Test
    void withdraw_failure_shouldReturnError() {

        when(restTemplate.postForEntity(anyString(), isNull(), eq(Object.class)))
                .thenThrow(new RuntimeException("error"));

        var result = userClient.withdraw(1L, new BigDecimal("10.00"));

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.USER_SERVICE_BAD_RESPONSE,
                result.getFailureData().get());
    }
}