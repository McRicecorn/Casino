package de.casino.banking_service.stat.StatController;

import de.casino.banking_service.common.Result;
import de.casino.banking_service.stat.Responses.statResponses.GetGameStatsResponse;
import de.casino.banking_service.stat.Responses.statResponses.GetGlobalStatResponse;
import de.casino.banking_service.stat.StatHandler.StatHandler;
import de.casino.banking_service.stat.Utility.ErrorWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatControllerTest {

    private StatHandler statHandler;
    private StatController controller;

    @BeforeEach
    void setUp() {
        statHandler = mock(StatHandler.class);
        controller = new StatController(statHandler);
    }


    @Test
    void getUserStats_success_shouldReturnOk() {

        Long userId = 1L;

        GetGlobalStatResponse response = new GetGlobalStatResponse(
                BigDecimal.TEN,
                BigDecimal.ONE,
                5,
                BigDecimal.valueOf(9)
        );

        when(statHandler.getGlobalStatByUserId(userId))
                .thenReturn(Result.success(response));

        var result = controller.getUserStats(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());

        verify(statHandler).getGlobalStatByUserId(userId);
    }

    @Test
    void getUserStats_failure_shouldReturnError() {

        Long userId = 1L;
        ErrorWrapper error = mock(ErrorWrapper.class);

        when(statHandler.getGlobalStatByUserId(userId))
                .thenReturn(Result.failure(error));

        when(error.getHttpStatus()).thenReturn(HttpStatus.NOT_FOUND);
        when(error.getMessage()).thenReturn("User not found");

        var result = controller.getUserStats(userId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("User not found", result.getBody());

        verify(statHandler).getGlobalStatByUserId(userId);
    }


    @Test
    void getGlobalStats_success_shouldReturnOk() {

        GetGlobalStatResponse response = new GetGlobalStatResponse(
                BigDecimal.TEN,
                BigDecimal.ONE,
                10,
                BigDecimal.valueOf(9)
        );

        when(statHandler.getGlobalStat())
                .thenReturn(Result.success(response));

        var result = controller.getGlobalStats();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());

        verify(statHandler).getGlobalStat();
    }

    @Test
    void getGlobalStats_failure_shouldReturnError() {

        ErrorWrapper error = mock(ErrorWrapper.class);

        when(statHandler.getGlobalStat())
                .thenReturn(Result.failure(error));

        when(error.getHttpStatus()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(error.getMessage()).thenReturn("Service error");

        var result = controller.getGlobalStats();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Service error", result.getBody());

        verify(statHandler).getGlobalStat();
    }


    @Test
    void getGameStatsByUser_success_shouldReturnOk() {

        String game = "ROULETTE";
        Long userId = 1L;

        GetGameStatsResponse response = mock(GetGameStatsResponse.class);

        when(statHandler.getGameStats(game, userId))
                .thenReturn(Result.success(response));

        var result = controller.getGameStatsByUserId(game, userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());

        verify(statHandler).getGameStats(game, userId);
    }

    @Test
    void getGameStatsByUser_failure_shouldReturnError() {

        String game = "ROULETTE";
        Long userId = 1L;

        ErrorWrapper error = mock(ErrorWrapper.class);

        when(statHandler.getGameStats(game, userId))
                .thenReturn(Result.failure(error));

        when(error.getHttpStatus()).thenReturn(HttpStatus.BAD_REQUEST);
        when(error.getMessage()).thenReturn("Invalid game");

        var result = controller.getGameStatsByUserId(game, userId);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid game", result.getBody());

        verify(statHandler).getGameStats(game, userId);
    }


    @Test
    void getGameStats_success_shouldReturnOk() {

        String game = "SLOTS";
        GetGameStatsResponse response = mock(GetGameStatsResponse.class);

        when(statHandler.getGameStats(game))
                .thenReturn(Result.success(response));

        var result = controller.getGameStats(game);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());

        verify(statHandler).getGameStats(game);
    }

    @Test
    void getGameStats_failure_shouldReturnError() {

        String game = "SLOTS";
        ErrorWrapper error = mock(ErrorWrapper.class);

        when(statHandler.getGameStats(game))
                .thenReturn(Result.failure(error));

        when(error.getHttpStatus()).thenReturn(HttpStatus.NOT_FOUND);
        when(error.getMessage()).thenReturn("Game not found");

        var result = controller.getGameStats(game);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Game not found", result.getBody());

        verify(statHandler).getGameStats(game);
    }
}