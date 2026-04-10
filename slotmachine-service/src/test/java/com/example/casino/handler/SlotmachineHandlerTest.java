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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;

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

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(handler, "bankingUrl", "http://localhost:8080/casino/bank/api/");
        ReflectionTestUtils.setField(handler, "serviceName", "Slotmachine-Service");
    }

    @Test
    @DisplayName("Should return an error if the input is 0 or negative")
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
    @DisplayName("Should return an error if the balance is less than bet amount")
    void play_Failure_InsufficientBalance(){
        //instantiate request with betAmount bigger than bank balance
        //betAmount is 10
        ISlotmachineRequest request = new SlotmachineRequest(1L, BigDecimal.TEN);

        //bank balance is 5
        BankingUserResponse user = new BankingUserResponse(1L, "otto", "müller", BigDecimal.valueOf(5));

        //restTemplate should return 'user' because we have no real communication with banking service
        when(restTemplate.getForObject(contains("localhost:8080"), eq(BankingUserResponse.class)))
                .thenReturn(user);

        var result = handler.play(request);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.INSUFFICIENT_BALANCE, result.getFailureData().get());

        verify(restTemplate).getForObject(anyString(), eq(BankingUserResponse.class));
        verifyNoInteractions(repository, modelFactory, responseFactory);
    }
    @Test
    @DisplayName("play should return error when banking service is not available")
    void play_Failure_BankingServiceNotAvailable(){
        //setup
        ISlotmachineRequest request = new SlotmachineRequest(1L, BigDecimal.valueOf(10));

        when(restTemplate.getForObject(anyString(), eq(BankingUserResponse.class)))
                .thenThrow(new RuntimeException("Banking Service currently not available"));

        //test
        var result = handler.play(request);

        //assert isFailure and correct ErrorWrapper
        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.UNEXPECTED_INTERNAL_ERROR, result.getFailureData().get());

        verify(restTemplate).getForObject(contains("localhost:8080"), eq(BankingUserResponse.class));
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Play is successful with no price won. Should return success with response data")
    void play_Success_NoWin(){
        ISlotmachineRequest request = new SlotmachineRequest(1L, BigDecimal.TEN);

        //user Response as a mock instead of real object
        BankingUserResponse user = mock(BankingUserResponse.class);
        when(user.getBalance()).thenReturn(BigDecimal.valueOf(100));

        when(restTemplate.getForObject(contains("localhost:8080"), eq(BankingUserResponse.class)))
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
        when(restTemplate.postForObject(contains("localhost:8080"), any(), eq(Object.class)))
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
    @DisplayName("Play is successful with Jackpot won. Should return success with response data")
    void play_Success_Jackpot(){
        //bet amount = 50, userId = 1
        BigDecimal betAmount= BigDecimal.valueOf(50);
        ISlotmachineRequest request = new SlotmachineRequest(1L, betAmount);

        //user Response as a mock instead of real object
        BankingUserResponse user = mock(BankingUserResponse.class);
        when(user.getBalance()).thenReturn(BigDecimal.valueOf(100));

        when(restTemplate.getForObject(contains("localhost:8080"), eq(BankingUserResponse.class)))
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
    @DisplayName("Play is successful with small price won. Should return success with response data")
    void play_Success_SmallWin(){
        //bet amount = 50, userId = 1
        BigDecimal betAmount= BigDecimal.valueOf(50);
        ISlotmachineRequest request = new SlotmachineRequest(1L, betAmount);

        //user Response as a mock instead of real object
        BankingUserResponse user = mock(BankingUserResponse.class);
        when(user.getBalance()).thenReturn(BigDecimal.valueOf(100));

        when(restTemplate.getForObject(contains("localhost:8080"), eq(BankingUserResponse.class)))
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
        when(restTemplate.postForObject(contains("localhost:8080"), any(), eq(Object.class)))
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
    @DisplayName("Play is successful. Asserts that the net amount has been correctly transferred to the banking service")
    void play_netAmountSuccessfullyTransferred(){
        //setup
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
        when(restTemplate.postForObject(contains("localhost:8080"), any(), eq(Object.class)))
                .thenReturn(new Object());

        //Test
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
        //assert that expected netAmount is actual netAmount
        assertEquals(expectedNetAmount, capturedRequest.getAmount());
    }

    @Test
    @DisplayName("readAllGames returns Success with empty list, as no games have been played yet")
    void readAllGames_Success_EmptyList() {
        //setup
        List<SlotmachineGameEntity> emptyList = List.of();
        when(repository.findAll()).thenReturn(emptyList);

        //test
        var result = handler.readAllGames();
        Iterable<ISlotmachineResponse> data = result.getSuccessData().get();

        //assert
        assertTrue(result.isSuccess());
        assertNotNull(data);

        //assert that list and response data is equal in size
        long dataSize = StreamSupport.stream(data.spliterator(), false).count();
        assertEquals(emptyList.size(), dataSize);

        verify(repository).findAll();
        //no responses should be created, as there are none
        verifyNoInteractions(responseFactory);
    }

    @Test
    @DisplayName("readAllGames returns Success with Response data")
    void readAllGames_Success(){
        //setup
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
    @DisplayName("readGame returns error because repository is empty")
    void readGame_Failure(){
        //setup
        when(repository.findById(1L)).thenReturn(Optional.empty());

        //test
        var result = handler.readGame(1L);

        //assertions
        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.GAME_NOT_FOUND, result.getFailureData().get());

        verify(repository).findById(1L);
        verifyNoInteractions(responseFactory);
    }

    @Test
    @DisplayName("readGame returns Success with game entity response data")
    void readGame_Success() {
        //mock game entity and response
        SlotmachineGameEntity gameEntity = mock(SlotmachineGameEntity.class);
        ISlotmachineResponse response = mock(ISlotmachineResponse.class);

        when(repository.findById(1L)).thenReturn(Optional.of(gameEntity));
        when(responseFactory.createSlotmachineResponse(gameEntity)).thenReturn(response);

        //test
        var result = handler.readGame(1L);

        //assert
        assertTrue(result.isSuccess());
        assertEquals(response, result.getSuccessData().get());

        verify(repository).findById(1L);
        verify(responseFactory).createSlotmachineResponse(gameEntity);
    }

    @Test
    @DisplayName("calculateChances returns success with String")
    void calculateChances() {
        //Test
        var result = handler.calculateChances();

        //assertions
        assertTrue(result.isSuccess());

        String chances = result.getSuccessData().get();

        assertNotNull(chances);
        assertTrue(chances.contains("as follows") && chances.contains("Small Win") && chances.contains("RTP"));
    }

    @Test
    @DisplayName("getRules returns the result as a string containing the explained rules")
    void getRules() {
        //Test
        var result = handler.getRules();

        //assertions
        assertTrue(result.isSuccess());
        String rules = result.getSuccessData().get();

        assertNotNull(rules);
        assertTrue(rules.contains("Jackpot") && rules.contains("matching symbols"));
    }

    @Test
    @DisplayName("readUserStats returns Error NO_GAMES_FOUND, since User doesn't exist in list (empty)")
    void readUserStats_Failure(){
        //setup
        long userId = 1L;
        //returns empty list
        when(repository.findByUserId(userId)).thenReturn(List.of());

        //test
        var result = handler.readUserStats(userId);

        //assert
        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.NO_GAMES_FOUND, result.getFailureData().get());

        verify(repository).findByUserId(userId);
        verifyNoInteractions(responseFactory);
    }

    @Test
    @DisplayName("readUserStats returns Success and user statistics when games for the given user ID are found")
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
    @DisplayName("readGlobalStats should return error when no games are found in the database")
    void readGlobalStats_Failure() {
        //setup
        when(repository.findAll()).thenReturn(List.of());

        //test
        var result = handler.readGlobalStats();

        //assert
        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.NO_GAMES_FOUND, result.getFailureData().get());

        verify(repository).findAll();
        verifyNoInteractions(responseFactory);
    }

    @Test
    @DisplayName("readGlobalStats should return success when games are found in database")
    void readGlobalStats_Success(){
        //mock game entity and create List
        SlotmachineGameEntity game1 = mock(SlotmachineGameEntity.class);
        List<SlotmachineGameEntity> mockGames = List.of(game1);
        //mock UserStatsResponse
        IGlobalStatsResponse mockStatsResponse = mock(IGlobalStatsResponse.class);

        when(repository.findAll()).thenReturn(mockGames);
        when(responseFactory.createGlobalStatsResponse(mockGames)).thenReturn(mockStatsResponse);

        //test
        var result = handler.readGlobalStats();

        //assertions
        assertTrue(result.isSuccess());
        assertEquals(mockStatsResponse, result.getSuccessData().get());

        verify(repository).findAll();
        verify(responseFactory).createGlobalStatsResponse(mockGames);
    }

    @Test
    @DisplayName("Should return failure when modelFactory fails to create the game entity")
    void play_Failure_ModelFactoryCreationFailed() {
        // setup
        BigDecimal betAmount = BigDecimal.valueOf(10);
        ISlotmachineRequest request = new SlotmachineRequest(1L, betAmount);

        BankingUserResponse user = mock(BankingUserResponse.class);
        when(user.getBalance()).thenReturn(BigDecimal.valueOf(100));
        when(restTemplate.getForObject(anyString(), eq(BankingUserResponse.class)))
                .thenReturn(user);

        when(random.nextInt(anyInt())).thenReturn(0, 1, 2);

        when(restTemplate.postForObject(anyString(), any(), eq(Object.class)))
                .thenReturn(new Object());

        ErrorWrapper factoryError = ErrorWrapper.UNEXPECTED_INTERNAL_ERROR;
        when(modelFactory.createSlotmachine(anyLong(), any(), any(), anyBoolean(), anyString()))
                .thenReturn(Result.failure(factoryError));

        //test
        var result = handler.play(request);

        // Assert
        assertTrue(result.isFailure(), "Result should be a failure");
        assertEquals(factoryError, result.getFailureData().get(), "Should return the error from the factory");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should return unexpected internal error when transaction posting fails")
    void play_Failure_TransactionPostFails() {
        //setup
        BigDecimal betAmount = BigDecimal.valueOf(10);
        ISlotmachineRequest request = new SlotmachineRequest(1L, betAmount);

        BankingUserResponse user = mock(BankingUserResponse.class);
        when(user.getBalance()).thenReturn(BigDecimal.valueOf(100));
        when(restTemplate.getForObject(anyString(), eq(BankingUserResponse.class)))
                .thenReturn(user);

        when(random.nextInt(anyInt())).thenReturn(0, 1, 2);

        when(restTemplate.postForObject(contains("transaction"), any(), eq(Object.class)))
                .thenThrow(new RuntimeException("Database connection lost during transaction"));

        //test
        var result = handler.play(request);

        //assert
        assertTrue(result.isFailure(), "Result should be a failure");
        assertEquals(ErrorWrapper.UNEXPECTED_INTERNAL_ERROR, result.getFailureData().get());

        verify(restTemplate).postForObject(anyString(), any(), any());
        verifyNoInteractions(repository, modelFactory);
    }

    @Test
    @DisplayName("Should return user not found error when banking service returns null")
    void play_Failure_UserNotFound() {
        //setup
        BigDecimal betAmount = BigDecimal.valueOf(10);
        ISlotmachineRequest request = new SlotmachineRequest(1L, betAmount);

        when(restTemplate.getForObject(anyString(), eq(BankingUserResponse.class)))
                .thenReturn(null);

        //test
        var result = handler.play(request);

        // assert
        assertTrue(result.isFailure(), "Result should be a failure");
        assertEquals(ErrorWrapper.USER_NOT_FOUND, result.getFailureData().get());

        verify(restTemplate).getForObject(anyString(), eq(BankingUserResponse.class));
        verifyNoInteractions(random, repository, modelFactory, responseFactory);
    }
}