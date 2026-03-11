package com.example.casino.controller;

import com.example.casino.handler.ISlotmachineHandler;
import com.example.casino.request.ISlotmachineRequest;
import com.example.casino.request.SlotmachineRequest;
import com.example.casino.response.ISlotmachineResponse;
import com.example.casino.utility.ErrorWrapper;
import com.example.casino.utility.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

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
    void readGame() {
    }

    @Test
    void readAllGames() {
    }

    @Test
    void readRules() {
    }

    @Test
    void readChances() {
    }

    @Test
    void readGlobalStats() {
    }

    @Test
    void readUserStats() {
    }
}