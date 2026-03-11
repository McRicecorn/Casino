package com.example.casino.controller;

import com.example.casino.dto.stats.IGlobalStatsResponse;
import com.example.casino.dto.stats.IUserStatsResponse;
import com.example.casino.handler.ISlotmachineHandler;
import com.example.casino.model.ISlotmachineGameEntity;
import com.example.casino.request.ISlotmachineRequest;
import com.example.casino.request.SlotmachineRequest;
import com.example.casino.response.ISlotmachineResponse;
import com.example.casino.response.SlotmachineResponse;
import com.example.casino.utility.ErrorWrapper;
import com.example.casino.utility.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SlotmachineControllerTest {
    private SlotmachineController controller;
    private ISlotmachineHandler mockHandler;

    @BeforeEach
    public void setUp() {
        mockHandler = mock(ISlotmachineHandler.class);
        controller = new SlotmachineController(mockHandler);
    }

    @Test
    @DisplayName("play returns 200 OK and response when successful")
    void play_Success() {
        //Setup
        SlotmachineRequest mockRequest = mock(SlotmachineRequest.class);
        ISlotmachineResponse mockResponse = mock(ISlotmachineResponse.class);
        when(mockHandler.play(mockRequest)).thenReturn(Result.success(mockResponse));

        //test
        ResponseEntity<?> response = controller.play(mockRequest);

        //assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(mockHandler).play(mockRequest);
    }

    @Test
    @DisplayName("play returns error status when handler fails")
    void play_Failure() {
        //setup
        SlotmachineRequest request = new SlotmachineRequest(1L, BigDecimal.TEN);
        //mock handler and return Result with Error
        when(mockHandler.play(any())).thenReturn(Result.failure(ErrorWrapper.INSUFFICIENT_BALANCE));

        //test
        ResponseEntity<?> response = controller.play(request);

        //assert that ErrorWrapper Insufficient_Balance is mapped to correct HttpStatus and Body
        assertEquals(HttpStatus.PAYMENT_REQUIRED, response.getStatusCode());
        assertEquals("Insufficient funds in your account.", response.getBody());
    }

    @Test
    @DisplayName("readGame returns 200 OK with game data")
    void readGame_Success() {
        //setup
        long id = 1L;
        ISlotmachineResponse mockResponse = mock(ISlotmachineResponse.class);
        when(mockHandler.readGame(id)).thenReturn(Result.success(mockResponse));

        //test
        ResponseEntity<?> responseEntity = controller.readGame(id);

        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());
        verify(mockHandler).readGame(id);
    }

    @Test
    @DisplayName("readGame returns Failure when no corresponding game exists for ID")
    void readGame_Failure(){
        //setup
        long id = 999L;
        when(mockHandler.readGame(id)).thenReturn(Result.failure(ErrorWrapper.GAME_NOT_FOUND));

        //test
        ResponseEntity<?> responseEntity = controller.readGame(id);

        //assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ErrorWrapper.GAME_NOT_FOUND.getMessage(), responseEntity.getBody());

        verify(mockHandler).readGame(id);
    }

    @Test
    @DisplayName("readAllGames returns 200 OK with a list of games")
    void readAllGames_Success() {
        //setup
        ISlotmachineResponse mockResponse = mock(ISlotmachineResponse.class);
        List<ISlotmachineResponse> responseList = List.of(mockResponse);
        when(mockHandler.readAllGames()).thenReturn(Result.success(responseList));

        //test
        ResponseEntity<?> responseEntity = controller.readAllGames();

        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseList, responseEntity.getBody());

        verify(mockHandler).readAllGames();
    }

    @Test
    @DisplayName("readAllGames returns 200 OK with empty list")
    void readAllGames_Success_EmptyList(){
        List<ISlotmachineResponse> responseList = List.of();
        when(mockHandler.readAllGames()).thenReturn(Result.success(responseList));

        //test
        ResponseEntity<?> responseEntity = controller.readAllGames();

        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseList, responseEntity.getBody());

        verify(mockHandler).readAllGames();
    }

    @Test
    @DisplayName("readAllGames returns 404 NOT FOUND when a technical error occurs")
    void readAllGames_Failure() {
        //setup
        when(mockHandler.readAllGames()).thenReturn(Result.failure(ErrorWrapper.NO_GAMES_FOUND));

        //test
        ResponseEntity<?> responseEntity = controller.readAllGames();

        //assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ErrorWrapper.NO_GAMES_FOUND.getMessage(), responseEntity.getBody());
    }

    @Test
    @DisplayName("readRules returns 200 OK with rules string")
    void readRules_Success() {
        //setUp
        String rules = "Some rules";
        when(mockHandler.getRules()).thenReturn(Result.success(rules));

        //test
        ResponseEntity<?> responseEntity = controller.readRules();

        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(rules, responseEntity.getBody());

        verify(mockHandler).getRules();
    }

    @Test
    @DisplayName("readRules returns 400 INTERNAL_SERVER_ERROR when rules cannot be loaded")
    void readRules_Failure() {
        //setup
        when(mockHandler.getRules()).thenReturn(Result.failure(ErrorWrapper.UNEXPECTED_INTERNAL_ERROR));

        //test
        ResponseEntity<?> responseEntity = controller.readRules();

        //assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(ErrorWrapper.UNEXPECTED_INTERNAL_ERROR.getMessage(), responseEntity.getBody());
    }

    @Test
    @DisplayName("readChances returns 200 OK with chances string")
    void readChances_Success() {
        //setup
        String chances = "Some chances";
        when(mockHandler.calculateChances()).thenReturn(Result.success(chances));

        //test
        ResponseEntity<?> responseEntity = controller.readChances();

        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(chances, responseEntity.getBody());

        verify(mockHandler).calculateChances();
    }

    @Test
    @DisplayName("readChances returns 400 INTERNAL_SERVER_ERROR when chances cannot be calculated")
    void readChances_Failure() {
        //setup
        when(mockHandler.calculateChances()).thenReturn(Result.failure(ErrorWrapper.UNEXPECTED_INTERNAL_ERROR));

        //test
        ResponseEntity<?> responseEntity = controller.readChances();

        //assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(ErrorWrapper.UNEXPECTED_INTERNAL_ERROR.getMessage(), responseEntity.getBody());
    }

    @Test
    @DisplayName("readGlobalStats returns 200 OK with stats data for all games played")
    void readGlobalStats_Success() {
        //setup
        IGlobalStatsResponse mockStats = mock(IGlobalStatsResponse.class);
        when(mockHandler.readGlobalStats()).thenReturn(Result.success(mockStats));

        //test
        ResponseEntity<?> responseEntity = controller.readGlobalStats();

        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockStats, responseEntity.getBody());

        verify(mockHandler).readGlobalStats();
    }

    @Test
    @DisplayName("readGlobalStats returns 404 NOT FOUND when no games exist")
    void readGlobalStats_Failure() {
        //setup
        when(mockHandler.readGlobalStats()).thenReturn(Result.failure(ErrorWrapper.NO_GAMES_FOUND));

        //test
        ResponseEntity<?> responseEntity = controller.readGlobalStats();

        //assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ErrorWrapper.NO_GAMES_FOUND.getMessage(), responseEntity.getBody());
    }

    @Test
    @DisplayName("readUserStats returns 200 OK with user stats data")
    void readUserStats_Success() {
        //setup
        long userId = 1L;
        IUserStatsResponse mockStats = mock(IUserStatsResponse.class);
        when(mockHandler.readUserStats(userId)).thenReturn(Result.success(mockStats));

        //test
        ResponseEntity<?> responseEntity = controller.readUserStats(userId);

        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockStats, responseEntity.getBody());
    }

    @Test
    @DisplayName("readUserStats returns 404 NOT FOUND for unknown user or no games")
    void readUserStats_Failure() {
        //setup
        long userId = 1L;
        when(mockHandler.readUserStats(userId)).thenReturn(Result.failure(ErrorWrapper.NO_GAMES_FOUND));

        //test
        ResponseEntity<?> responseEntity = controller.readUserStats(userId);

        //assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ErrorWrapper.NO_GAMES_FOUND.getMessage(), responseEntity.getBody());
    }
}