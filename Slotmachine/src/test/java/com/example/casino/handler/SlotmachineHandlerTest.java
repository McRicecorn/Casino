package com.example.casino.handler;

import com.example.casino.dto.transaction.BankingTransactionRequest;
import com.example.casino.dto.user.BankingUserResponse;
import com.example.casino.factory.ISlotmachineGameFactory;
import com.example.casino.model.SlotmachineGameEntity;
import com.example.casino.repository.ISlotmachineRepository;
import com.example.casino.request.ISlotmachineRequest;
import com.example.casino.request.SlotmachineRequest;
import com.example.casino.response.ISlotmachineResponse;
import com.example.casino.responseFactory.ISlotmachineResponseFactory;
import com.example.casino.utility.ErrorWrapper;
import com.example.casino.utility.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SlotmachineHandlerTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Random random;

    @Mock
    private ISlotmachineRepository repository;

    @Mock
    private ISlotmachineGameFactory modelFactory;

    @Mock
    private ISlotmachineResponseFactory responseFactory;

    @InjectMocks
    private SlotmachineHandler handler;

    private long getRandomId() {
        return ThreadLocalRandom.current().nextLong(1, 100000);
    }

    @Test
    void play_Failure_InvalidBetAmount() {
        //betAmount is 0
        BigDecimal invalidBet = BigDecimal.ZERO;

        //send request
        ISlotmachineRequest request = new SlotmachineRequest(1L, invalidBet);

        //act on request via handler
        var result = handler.play(request);

        //assertions
        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.INVALID_BET_AMOUNT, result.getFailureData().get());
        verifyNoInteractions(repository, modelFactory, responseFactory, restTemplate);
    }

    @Test
    void play_Failure_InsufficientBalance(){
        //instanciate request with betAmouont bigger than bank balance
        //betAmount is 10
        ISlotmachineRequest request = new SlotmachineRequest(1L, BigDecimal.TEN);

        //bank balance is 5
        BankingUserResponse user = new BankingUserResponse(1L, "otto", "müller", BigDecimal.valueOf(5));

        //restTemplate should return 'user' because we have no real communication with banking service
        when(restTemplate.getForObject(anyString(), eq(BankingUserResponse.class)))
                .thenReturn(user);

        var result = handler.play(request);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.INSUFFICIENT_BALANCE, result.getFailureData().get());

        verify(restTemplate).getForObject(anyString(), eq(BankingUserResponse.class));
        verifyNoInteractions(repository, modelFactory, responseFactory);
    }

    @Test
    void play_Success_NoWin(){
        ReflectionTestUtils.setField(handler, "bankingUrl", "http://localhost:8080/casino/bank/api/");
        ReflectionTestUtils.setField(handler, "serviceName", "Slotmachine-Service");

        ISlotmachineRequest request = new SlotmachineRequest(1L, BigDecimal.TEN);

        //user Response as a mock instead of real object
        BankingUserResponse user = mock(BankingUserResponse.class);
        when(user.getBalance()).thenReturn(BigDecimal.valueOf(100));

        when(restTemplate.getForObject(anyString(), eq(BankingUserResponse.class)))
                .thenReturn(user);

        //three different symbols == no win
        when(random.nextInt(anyInt())).thenReturn(0, 1, 2);

        SlotmachineGameEntity entity = mock(SlotmachineGameEntity.class);
        when(modelFactory.createSlotmachine(
                eq(1L),
                eq(BigDecimal.TEN),
                eq(BigDecimal.ZERO),
                eq(false),
                anyString()))
                .thenReturn(Result.success(entity));

        when(repository.save(entity)).thenReturn(entity);

        ISlotmachineResponse response = mock(ISlotmachineResponse.class);
        when(responseFactory.createSlotmachineResponse(entity)).thenReturn(response);

        //simulate http response
        when(restTemplate.postForObject(anyString(), any(), eq(Object.class)))
                .thenReturn(new Object());

        //test
        var result = handler.play(request);

        //assert true if it was successful
        assertTrue(result.isSuccess());
        //assert equals if handler returns response
        assertEquals(response, result.getSuccessData().get());

        verify(restTemplate).getForObject(contains("user/1"), eq(BankingUserResponse.class));
        verify(restTemplate).postForObject(contains("transaction/user/1"), any(), eq(Object.class));
        verify(modelFactory).createSlotmachine(eq(1L), eq(BigDecimal.TEN), eq(BigDecimal.ZERO), eq(false), anyString());
        verify(repository).save(entity);
        verify(responseFactory).createSlotmachineResponse(entity);
    }

    @Test
    void play_Success_Jackpot(){
        ReflectionTestUtils.setField(handler, "bankingUrl", "http://localhost:8080/casino/bank/api/");
        ReflectionTestUtils.setField(handler, "serviceName", "Slotmachine-Service");

        //bet amount = 50, userId = 1
        BigDecimal betAmount= BigDecimal.valueOf(50);
        ISlotmachineRequest request = new SlotmachineRequest(1L, betAmount);

        //user Response as a mock instead of real object
        BankingUserResponse user = mock(BankingUserResponse.class);
        when(user.getBalance()).thenReturn(BigDecimal.valueOf(100));

        when(restTemplate.getForObject(anyString(), eq(BankingUserResponse.class)))
                .thenReturn(user);

        //three same symbols == Jackpot == 10 * betAmount (10 * 50 = 500)
        BigDecimal expectedWinAmount = betAmount.multiply(BigDecimal.TEN);

        when(random.nextInt(anyInt())).thenReturn(1, 1, 1);

        SlotmachineGameEntity entity = mock(SlotmachineGameEntity.class);
        when(modelFactory.createSlotmachine(
                eq(1L),
                eq(betAmount),
                eq(expectedWinAmount),
                eq(true),
                anyString()))
                .thenReturn(Result.success(entity));

        when(repository.save(entity)).thenReturn(entity);

        ISlotmachineResponse response = mock(ISlotmachineResponse.class);
        when(responseFactory.createSlotmachineResponse(entity)).thenReturn(response);

        //simulate http response
        when(restTemplate.postForObject(anyString(), any(), eq(Object.class)))
                .thenReturn(new Object());

        //test
        var result = handler.play(request);

        //assert true if it was successful
        assertTrue(result.isSuccess());
        //assert equals if handler returns response
        assertEquals(response, result.getSuccessData().get());

        verify(restTemplate).getForObject(contains("user/1"), eq(BankingUserResponse.class));
        verify(restTemplate).postForObject(contains("transaction/user/1"), any(), eq(Object.class));
        verify(modelFactory).createSlotmachine(eq(1L), eq(betAmount), eq(expectedWinAmount), eq(true), anyString());
        verify(repository).save(entity);
        verify(responseFactory).createSlotmachineResponse(entity);
    }

    @Test
    void play_Success_SmallWin(){
        ReflectionTestUtils.setField(handler, "bankingUrl", "http://localhost:8080/casino/bank/api/");
        ReflectionTestUtils.setField(handler, "serviceName", "Slotmachine-Service");

        //bet amount = 50, userId = 1
        BigDecimal betAmount= BigDecimal.valueOf(50);
        ISlotmachineRequest request = new SlotmachineRequest(1L, betAmount);

        //user Response as a mock instead of real object
        BankingUserResponse user = mock(BankingUserResponse.class);
        when(user.getBalance()).thenReturn(BigDecimal.valueOf(100));

        when(restTemplate.getForObject(anyString(), eq(BankingUserResponse.class)))
                .thenReturn(user);

        //two same symbols == smallWin == 2 * betAmount (2 * 50 = 100)
        BigDecimal expectedWinAmount = betAmount.multiply(BigDecimal.valueOf(2));

        when(random.nextInt(anyInt())).thenReturn(1, 1, 0);

        SlotmachineGameEntity entity = mock(SlotmachineGameEntity.class);
        when(modelFactory.createSlotmachine(
                eq(1L),
                eq(betAmount),
                eq(expectedWinAmount),
                eq(true),
                anyString()))
                .thenReturn(Result.success(entity));

        when(repository.save(entity)).thenReturn(entity);

        ISlotmachineResponse response = mock(ISlotmachineResponse.class);
        when(responseFactory.createSlotmachineResponse(entity)).thenReturn(response);

        //simulate http response
        when(restTemplate.postForObject(anyString(), any(), eq(Object.class)))
                .thenReturn(new Object());

        //test
        var result = handler.play(request);

        //assert true if it was successful
        assertTrue(result.isSuccess());
        //assert equals if handler returns response
        assertEquals(response, result.getSuccessData().get());

        verify(restTemplate).getForObject(contains("user/1"), eq(BankingUserResponse.class));
        verify(restTemplate).postForObject(contains("transaction/user/1"), any(), eq(Object.class));
        verify(modelFactory).createSlotmachine(eq(1L), eq(betAmount), eq(expectedWinAmount), eq(true), anyString());
        verify(repository).save(entity);
        verify(responseFactory).createSlotmachineResponse(entity);
    }

    @Test
    void play_netAmountSuccessfullyTransmitted(){
        ReflectionTestUtils.setField(handler, "bankingUrl", "http://localhost:8080/casino/bank/api/");
        ReflectionTestUtils.setField(handler, "serviceName", "Slotmachine-Service");

        //bet amount = 50, userId = 1
        BigDecimal betAmount= BigDecimal.valueOf(50);
        ISlotmachineRequest request = new SlotmachineRequest(1L, betAmount);

        //user Response as a mock instead of real object
        BankingUserResponse user = mock(BankingUserResponse.class);
        when(user.getBalance()).thenReturn(BigDecimal.valueOf(100));

        when(restTemplate.getForObject(anyString(), eq(BankingUserResponse.class)))
                .thenReturn(user);

        //three same symbols == Jackpot == 10 * betAmount (10 * 50 = 500)
        BigDecimal expectedWinAmount = betAmount.multiply(BigDecimal.TEN);

        when(random.nextInt(anyInt())).thenReturn(1, 1, 1);

        SlotmachineGameEntity entity = mock(SlotmachineGameEntity.class);
        when(modelFactory.createSlotmachine(
                eq(1L),
                eq(betAmount),
                eq(expectedWinAmount),
                eq(true),
                anyString()))
                .thenReturn(Result.success(entity));

        when(repository.save(entity)).thenReturn(entity);

        ISlotmachineResponse response = mock(ISlotmachineResponse.class);
        when(responseFactory.createSlotmachineResponse(entity)).thenReturn(response);

        //simulate http response
        when(restTemplate.postForObject(anyString(), any(), eq(Object.class)))
                .thenReturn(new Object());

        //test
        var result = handler.play(request);

        //capture response to banking service (netAmount)
        BigDecimal expectedNetAmount = expectedWinAmount.subtract(betAmount);
        ArgumentCaptor<BankingTransactionRequest> captor =
                ArgumentCaptor.forClass(BankingTransactionRequest.class);

        verify(restTemplate).postForObject(
                contains("transaction/user/1"),
                captor.capture(),
                eq(Object.class)
        );

        BankingTransactionRequest capturedRequest = captor.getValue();

        assertTrue(result.isSuccess());
        //assert if expected netAmount is actual netAmount
        assertEquals(expectedNetAmount, capturedRequest.getAmount());
    }

    @Test
    void readAllGames_Failure() {
        Optional<SlotmachineGameEntity> emptyResult = Optional.empty();
        when(repository.findById(99L)).thenReturn(emptyResult);

        var result = handler.readGame(99L);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.GAME_NOT_FOUND, result.getFailureData().get());
        verify(repository).findById(99L);
        verifyNoInteractions(responseFactory);

    }

    @Test
    void readGame() {
    }

    @Test
    void calculateChances() {
    }

    @Test
    void getRules() {
    }

    @Test
    void readUserStats() {
    }

    @Test
    void readGlobalStats() {
    }
}