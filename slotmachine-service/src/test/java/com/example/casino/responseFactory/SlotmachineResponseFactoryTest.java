package com.example.casino.responseFactory;

import com.example.casino.dto.stats.IGlobalStatsResponse;
import com.example.casino.dto.stats.IUserStatsResponse;
import com.example.casino.model.ISlotmachineGameEntity;
import com.example.casino.response.ISlotmachineResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SlotmachineResponseFactoryTest {

    private SlotmachineResponseFactory factory;

    // static values
    private static long staticUserId;
    private static BigDecimal staticBet;

    //dynamic values
    private long randomUserId;
    private BigDecimal randomBet;

    //mock game entity
    private ISlotmachineGameEntity mockEntity;

    @BeforeAll
    public static void setUpAll() {
        staticUserId = 1L;
        staticBet = BigDecimal.TEN;
    }

    @BeforeEach
    public void setUp() {
        factory = new SlotmachineResponseFactory();

        randomUserId = (long) (Math.random() * 1000);
        randomBet = BigDecimal.valueOf(Math.random() * 100);

        mockEntity = mock(ISlotmachineGameEntity.class);
    }

    @Test
    @DisplayName("Factory creates a response with static values successfully.")
    void createSlotmachineResponse_WithStaticValues_NoWin() {
        //setup
        BigDecimal expectedWinAmount = BigDecimal.ZERO;

        when(mockEntity.getUserId()).thenReturn(staticUserId);
        when(mockEntity.getBetAmount()).thenReturn(staticBet);
        when(mockEntity.isWinning()).thenReturn(false);
        when(mockEntity.getSlotResult()).thenReturn("CHERRY, LEMON, PLUM");
        when(mockEntity.getTimestamp()).thenReturn(LocalDateTime.now());
        when(mockEntity.getWinAmount()).thenReturn(BigDecimal.ZERO);

        //test
        ISlotmachineResponse response = factory.createSlotmachineResponse(mockEntity);

        BigDecimal expectedProfit = expectedWinAmount.subtract(staticBet);

        //assert
        assertNotNull(response);
        assertEquals(staticUserId, response.getUser(), "User ID should match static userId.");
        assertTrue(expectedProfit.compareTo(response.getAmount()) == 0, "Amount=Profit --> should match winAmount(0) - betAmount(10) = -10");
        assertTrue(response.getMessage().contains("Nothing won"));
    }

    @Test
    @DisplayName("Factory creates response correctly for a Small Win (2x)")
    void createSlotmachineResponse_WithStaticValues_SmallWin() {
        //setup
        BigDecimal winAmount = staticBet.multiply(BigDecimal.valueOf(2));

        when(mockEntity.getUserId()).thenReturn(staticUserId);
        when(mockEntity.getBetAmount()).thenReturn(staticBet);
        when(mockEntity.isWinning()).thenReturn(true);
        when(mockEntity.getSlotResult()).thenReturn("CHERRY, CHERRY, PLUM");
        when(mockEntity.getTimestamp()).thenReturn(LocalDateTime.now());
        when(mockEntity.getWinAmount()).thenReturn(winAmount);

        //test
        ISlotmachineResponse response = factory.createSlotmachineResponse(mockEntity);


        BigDecimal expectedProfit = winAmount.subtract(staticBet);

        //assert
        assertNotNull(response);
        assertTrue(expectedProfit.compareTo(response.getAmount()) == 0, "Profit should be 10 (20-10)");
        assertTrue(response.getMessage().contains("You WON! Your bank balance will thank you."), "Message should indicate a small win");
    }

    @Test
    @DisplayName("Factory creates response correctly for a Small Win (2x)")
    void createSlotmachineResponse_WithStaticValues_Jackpot() {
        //setup
        BigDecimal winAmount = staticBet.multiply(BigDecimal.valueOf(10));

        when(mockEntity.getUserId()).thenReturn(staticUserId);
        when(mockEntity.getBetAmount()).thenReturn(staticBet);
        when(mockEntity.isWinning()).thenReturn(true);
        when(mockEntity.getSlotResult()).thenReturn("CHERRY, CHERRY, CHERRY");
        when(mockEntity.getTimestamp()).thenReturn(LocalDateTime.now());
        when(mockEntity.getWinAmount()).thenReturn(winAmount);

        //test
        ISlotmachineResponse response = factory.createSlotmachineResponse(mockEntity);


        BigDecimal expectedProfit = winAmount.subtract(staticBet);

        //assert
        assertNotNull(response);
        assertTrue(expectedProfit.compareTo(response.getAmount()) == 0, "Profit should be 90 (100-10)");
        assertTrue(BigDecimal.valueOf(90).compareTo(response.getAmount()) == 0, "Profit should be 90 (100-10)");
        assertTrue(response.getMessage().contains("FANTASTIC! That was an epic WIN! Congrats"), "Message should indicate winning the jackpot");
    }


    @Test
    @DisplayName("Factory calls the entity methods exactly once.")
    void createSlotmachineResponse_ShouldCallEntityMethodsOnce() {
        //setup
        when(mockEntity.getUserId()).thenReturn(randomUserId);
        when(mockEntity.getBetAmount()).thenReturn(randomBet);
        when(mockEntity.isWinning()).thenReturn(false);
        when(mockEntity.getSlotResult()).thenReturn("CHERRY, LEMON, PLUM");
        when(mockEntity.getTimestamp()).thenReturn(LocalDateTime.now());
        when(mockEntity.getWinAmount()).thenReturn(BigDecimal.ZERO);

        //test
        factory.createSlotmachineResponse(mockEntity);

        //assert
        verify(mockEntity, times(1)).getUserId();
        verify(mockEntity, times(1)).getBetAmount();
    }

    @Test
    @DisplayName("Factory creates a response with random values successfully.")
    void createSlotmachineResponse_WithRandomValues() {
        //setup
        BigDecimal winAmount = randomBet.multiply(BigDecimal.valueOf(10));

        when(mockEntity.getUserId()).thenReturn(randomUserId);
        when(mockEntity.getBetAmount()).thenReturn(randomBet);
        when(mockEntity.getWinAmount()).thenReturn(winAmount);
        when(mockEntity.isWinning()).thenReturn(true);
        when(mockEntity.getSlotResult()).thenReturn("RANDOM, SYMBOLS, HERE");

        //test
        ISlotmachineResponse response = factory.createSlotmachineResponse(mockEntity);

        //assert
        assertEquals(randomUserId, response.getUser(), "Random User ID should match.");

        BigDecimal expectedProfit = winAmount.subtract(randomBet);

        assertTrue(expectedProfit.compareTo(response.getAmount()) == 0);
    }

    @Test
    @DisplayName("createUserStatsResponse calculates user statistics correctly")
    void createUserStatsResponse_Success() {
        //setup
        //two game entities
        ISlotmachineGameEntity game1 = mock(ISlotmachineGameEntity.class);
        when(game1.getUserId()).thenReturn(1L);
        when(game1.getBetAmount()).thenReturn(new BigDecimal("10.00"));
        when(game1.getWinAmount()).thenReturn(new BigDecimal("25.00"));
        when(game1.isWinning()).thenReturn(true);

        ISlotmachineGameEntity game2 = mock(ISlotmachineGameEntity.class);
        when(game2.getUserId()).thenReturn(1L);
        when(game2.getBetAmount()).thenReturn(new BigDecimal("10.00"));
        when(game2.getWinAmount()).thenReturn(BigDecimal.ZERO);
        when(game2.isWinning()).thenReturn(false);

        //create list
        List<ISlotmachineGameEntity> games = List.of(game1, game2);

        //test
        IUserStatsResponse response = factory.createUserStatsResponse(games);

        //assert
        assertEquals(1L, response.getClientId());
        assertEquals(2, response.getTotalGamesCount(), "Total games should be 2");
        assertEquals(1, response.getTotalWinnings(), "Winnings should be 1");
        assertEquals(1, response.getTotalLosses(), "Losses should be 1");

        //turnover: 10 + 10 = 20
        assertEquals(new BigDecimal("20.00"), response.getTotalTurnoverFromClient());
        //client profit: (25 + 0) - (10 + 10) = 5
        assertEquals(new BigDecimal("5.00"), response.getTotalClientProfit());
        //house profit: (10 + 10) - (25 + 0) = -5
        assertEquals(new BigDecimal("-5.00"), response.getTotalHouseProfitFromClient());
    }

    @Test
    @DisplayName("createGlobalStatsResponse calculates global statistics correctly")
    void createGlobalStatsResponse_Success() {
        //setup
        //user 1 played two games; user 2 played one game
        ISlotmachineGameEntity g1 = mock(ISlotmachineGameEntity.class);
        when(g1.getUserId()).thenReturn(1L);
        when(g1.getBetAmount()).thenReturn(new BigDecimal("50.00"));
        when(g1.getWinAmount()).thenReturn(new BigDecimal("10.00"));

        ISlotmachineGameEntity g2 = mock(ISlotmachineGameEntity.class);
        when(g2.getUserId()).thenReturn(1L);
        when(g2.getBetAmount()).thenReturn(new BigDecimal("50.00"));
        when(g2.getWinAmount()).thenReturn(new BigDecimal("10.00"));

        ISlotmachineGameEntity g3 = mock(ISlotmachineGameEntity.class);
        when(g3.getUserId()).thenReturn(2L); // Anderer User!
        when(g3.getBetAmount()).thenReturn(new BigDecimal("100.00"));
        when(g3.getWinAmount()).thenReturn(BigDecimal.ZERO);

        //create list
        List<ISlotmachineGameEntity> allGames = List.of(g1, g2, g3);

        //test
        IGlobalStatsResponse response = factory.createGlobalStatsResponse(allGames);

        //assert
        assertEquals(2, response.getTotalClientCount(), "Should find 2 distinct users");
        assertEquals(3, response.getTotalGamesCount(), "Total games should be 3");

        //turnover: 50 + 50 + 100 = 200
        assertEquals(new BigDecimal("200.00"), response.getTotalTurnover());
        //CashOut: 10 + 10 + 0 = 20
        assertEquals(new BigDecimal("20.00"), response.getTotalCashOut());
        //profit (House): 200 - 20 = 180
        assertEquals(new BigDecimal("180.00"), response.getTotalProfit());
    }
}