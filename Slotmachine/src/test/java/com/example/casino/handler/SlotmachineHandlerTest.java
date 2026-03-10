package com.example.casino.handler;

import com.example.casino.factory.ISlotmachineGameFactory;
import com.example.casino.repository.ISlotmachineRepository;
import com.example.casino.request.ISlotmachineRequest;
import com.example.casino.responseFactory.ISlotmachineResponseFactory;
import com.example.casino.utility.ErrorWrapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SlotmachineHandlerTest {

    private static long staticUserId;
    private static BigDecimal staticBetAmount;

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ISlotmachineRepository repository;
    @Mock
    private ISlotmachineGameFactory gameFactory;
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
        //mock request
        ISlotmachineRequest request = mock(ISlotmachineRequest.class);
        when(request.getBetAmount()).thenReturn(invalidBet);

        // Act
        var result = handler.play(request);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.INVALID_BET_AMOUNT, result.getFailureData().get());
        verifyNoInteractions(restTemplate);
    }

    @Test
    void readAllGames() {
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