package de.casino.banking_service.stat.StatHandler;

import de.casino.banking_service.common.Games;
import de.casino.banking_service.stat.Clients.ITransactionClientStats;
import de.casino.banking_service.stat.Responses.statResponses.GetGameStatsResponse;
import de.casino.banking_service.stat.Responses.statResponses.GetGlobalStatResponse;
import de.casino.banking_service.stat.Responses.transactionResponses.GetAllTransactionsTClientResponse;
import de.casino.banking_service.stat.Utility.ErrorWrapper;
import de.casino.banking_service.common.Result;
import de.casino.banking_service.stat.responseFactory.responseFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatHandlerTest {

    @Mock
    private ITransactionClientStats tClient;


    @Mock
    private responseFactory responseFactory;

    @InjectMocks
    private StatHandler statHandler;




    @Test
    void getGlobalStatByUserId_shouldCalculateCorrectly() {

        long userId = 1L;

        GetAllTransactionsTClientResponse t1 = mock(GetAllTransactionsTClientResponse.class);
        GetAllTransactionsTClientResponse t2 = mock(GetAllTransactionsTClientResponse.class);
        GetAllTransactionsTClientResponse t3 = mock(GetAllTransactionsTClientResponse.class);

        when(t1.getAmount()).thenReturn(new BigDecimal("10"));
        when(t2.getAmount()).thenReturn(new BigDecimal("-5"));
        when(t3.getAmount()).thenReturn(new BigDecimal("20"));

        when(tClient.getAllTransactionsById(userId))
                .thenReturn(Result.success(List.of(t1, t2, t3)));

        when(responseFactory.createGetGlobalStatResponse(any(), any(), anyInt(), any()))
                .thenAnswer(inv -> new GetGlobalStatResponse(
                        inv.getArgument(0),
                        inv.getArgument(1),
                        inv.getArgument(2),
                        inv.getArgument(3)
                ));

        var result = statHandler.getGlobalStatByUserId(userId);

        assertTrue(result.isSuccess());

        var response = result.getSuccessData().get();

        assertEquals(new BigDecimal("30"), response.getTotalGained());
        assertEquals(new BigDecimal("5"), response.getTotalLost());
        assertEquals(3, response.getTotalTransactions());
        assertEquals(new BigDecimal("25"), response.getNet());
    }
    @Test
    void getGlobalStat_shouldCalculateCorrectly() {

        var transactions = List.of(
                new GetAllTransactionsTClientResponse(1, 1L, new BigDecimal("10"), Games.ROULETTE),
                new GetAllTransactionsTClientResponse(2, 2L, new BigDecimal("-20"), Games.SLOTS)
        );

        when(tClient.getAllTransactions())
                .thenReturn(Result.success(transactions));

        when(responseFactory.createGetGlobalStatResponse(any(), any(), anyInt(), any()))
                .thenAnswer(inv -> new GetGlobalStatResponse(
                        inv.getArgument(0),
                        inv.getArgument(1),
                        inv.getArgument(2),
                        inv.getArgument(3)
                ));

        var result = statHandler.getGlobalStat();

        assertTrue(result.isSuccess());

        var response = result.getSuccessData().get();

        assertEquals(new BigDecimal("10"), response.getTotalGained());
        assertEquals(new BigDecimal("20"), response.getTotalLost());
        assertEquals(2, response.getTotalTransactions());
        assertEquals(new BigDecimal("-10"), response.getNet());
    }
    @Test
    void getGlobalStat_shouldFail_whenClientFails() {

        when(tClient.getAllTransactions())
                .thenReturn(Result.failure(ErrorWrapper.EXTERNAL_SERVICE_ERROR));

        var result = statHandler.getGlobalStat();

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.EXTERNAL_SERVICE_ERROR,
                result.getFailureData().get());
    }

    @Test
    void getGameStatsByUser_shouldFilterCorrectGame() {

        long userId = 1L;

        var transactions = List.of(
                new GetAllTransactionsTClientResponse(1, userId, new BigDecimal("10"), Games.ROULETTE),
                new GetAllTransactionsTClientResponse(2, userId, new BigDecimal("-5"), Games.ROULETTE),
                new GetAllTransactionsTClientResponse(3, userId, new BigDecimal("20"), Games.SLOTS)
        );

        when(tClient.getAllTransactionsById(userId))
                .thenReturn(Result.success(transactions));

        when(responseFactory.createGetGameStatsResponse(
                anyInt(), anyInt(), anyInt(),
                any(), any(), eq(Games.ROULETTE)
        )).thenAnswer(inv -> new GetGameStatsResponse(
                inv.getArgument(0),
                inv.getArgument(1),
                inv.getArgument(2),
                inv.getArgument(3),
                inv.getArgument(4),
                inv.getArgument(5)
        ));

        var result = statHandler.getGameStats("ROULETTE", userId);

        assertTrue(result.isSuccess());

        var response = result.getSuccessData().get();

        assertEquals(2, response.getTotalGamesPlayed());
        assertEquals(1, response.getTotalGamesWon());
        assertEquals(1, response.getTotalGamesLost());
        assertEquals(new BigDecimal("10"), response.getTotalGain());
        assertEquals(new BigDecimal("5"), response.getTotalLoss());
    }

    @Test
    void getGameStats_shouldFail_whenGameInvalid() {

        long userId = 1L;

        var result = statHandler.getGameStats("INVALID_GAME", userId);

        assertTrue(result.isFailure());
        assertEquals(ErrorWrapper.INVOICING_PARTY_DOES_NOT_EXIST,
                result.getFailureData().get());
    }

    @Test
    void getGameStats_global_shouldWork() {

        var transactions = List.of(
                new GetAllTransactionsTClientResponse(1, 1L, new BigDecimal("10"), Games.ROULETTE),
                new GetAllTransactionsTClientResponse(2, 2L, new BigDecimal("-5"), Games.ROULETTE)
        );

        when(tClient.getAllTransactions())
                .thenReturn(Result.success(transactions));

        when(responseFactory.createGetGameStatsResponse(
                anyInt(), anyInt(), anyInt(),
                any(), any(), eq(Games.ROULETTE)
        )).thenAnswer(inv -> new GetGameStatsResponse(
                inv.getArgument(0),
                inv.getArgument(1),
                inv.getArgument(2),
                inv.getArgument(3),
                inv.getArgument(4),
                inv.getArgument(5)
        ));

        var result = statHandler.getGameStats("ROULETTE");

        assertTrue(result.isSuccess());

        var response = result.getSuccessData().get();

        assertEquals(2, response.getTotalGamesPlayed());
    }
}