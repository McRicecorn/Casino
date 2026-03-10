package com.example.casino.handler;

import com.example.casino.dto.stats.IGlobalStatsResponse;
import com.example.casino.dto.stats.IUserStatsResponse;
import com.example.casino.dto.transaction.BankingTransactionRequest;
import com.example.casino.dto.user.BankingUserResponse;
import com.example.casino.factory.ISlotmachineGameFactory;
import com.example.casino.model.ISlotmachineGameEntity;
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

import java.util.List;
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
        //instantiate request with betAmouont bigger than bank balance
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
    void readAllGames_Success(){

        SlotmachineGameEntity game1 = mock(SlotmachineGameEntity.class);
        SlotmachineGameEntity game2 = mock(SlotmachineGameEntity.class);

        //create list of game entities for repository
        List<SlotmachineGameEntity> gameEntities = List.of(game1, game2);

        //mock repository
        when(repository.findAll()).thenReturn(gameEntities);

        //mock responses (readAllGames() iterates over gameEntities and creates SlotmachineResponse for each)
        ISlotmachineResponse response1 = mock(ISlotmachineResponse.class);
        ISlotmachineResponse response2 = mock(ISlotmachineResponse.class);
        when(responseFactory.createSlotmachineResponse(game1)).thenReturn(response1);
        when(responseFactory.createSlotmachineResponse(game2)).thenReturn(response2);

        //test
        var result = handler.readAllGames();

        //assertions
        assertTrue(result.isSuccess());
        assertEquals(List.of(response1, response2), result.getSuccessData().get());

        verify(repository).findAll();
        verify(responseFactory).createSlotmachineResponse(game1);
        verify(responseFactory).createSlotmachineResponse(game2);
    }

    @Test
    void readGame_Success() {
        //mock game entity and response
        SlotmachineGameEntity gameEntity = mock(SlotmachineGameEntity.class);
        ISlotmachineResponse response = mock(ISlotmachineResponse.class);

        when(repository.findById(1L)).thenReturn(Optional.of(gameEntity));
        when(responseFactory.createSlotmachineResponse(gameEntity)).thenReturn(response);

        var result = handler.readGame(1L);

        assertTrue(result.isSuccess());
        assertEquals(response, result.getSuccessData().get());

        verify(repository).findById(1L);
        verify(responseFactory).createSlotmachineResponse(gameEntity);
    }

    @Test
    void calculateChances() {
    }

    @Test
    void getRules() {
        var result = handler.getRules();

        assertTrue(result.isSuccess());
        // Hier prüfst du auf den Text, den dein Handler tatsächlich zurückgibt
        String rules = result.getSuccessData().get();
        assertNotNull(rules);
        assertTrue(rules.contains("Jackpot") || rules.contains("Win"));
    }

    @Test
    void readUserStats_Success() {
        long userId = 1L;

        //mock game entity and create List because findbyUserId returns List
        ISlotmachineGameEntity game1 = mock(ISlotmachineGameEntity.class);
        List<ISlotmachineGameEntity> mockGames = List.of(game1);
        //mock UserStatsResponse
        IUserStatsResponse mockStatsResponse = mock(IUserStatsResponse.class);

        when(repository.findByUserId(userId)).thenReturn(mockGames);
        when(responseFactory.createUserStatsResponse(mockGames)).thenReturn(mockStatsResponse);

        //test
        var result = handler.readUserStats(userId);

        //assertions
        assertTrue(result.isSuccess());
        assertEquals(mockStatsResponse, result.getSuccessData().get());

        verify(repository).findByUserId(userId);
        verify(responseFactory).createUserStatsResponse(mockGames);

    }

    @Test
    void readGlobalStats() {
    }
}