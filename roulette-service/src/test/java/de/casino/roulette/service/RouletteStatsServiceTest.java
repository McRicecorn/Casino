package de.casino.roulette.service;

import de.casino.roulette.model.dto.GlobalStatsView;
import de.casino.roulette.model.dto.UserStatsView;
import de.casino.roulette.repository.RouletteGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RouletteStatsServiceTest {

  private RouletteGameRepository repo;
  private RouletteStatsService service;

  @BeforeEach
  void setUp() {
    repo = mock(RouletteGameRepository.class);
    service = new RouletteStatsService(repo);
  }

  @Test
  void globalStats_returnsAggregatedValues() {
    when(repo.count()).thenReturn(10L);
    when(repo.countDistinctUsers()).thenReturn(3L);
    when(repo.sumTurnover()).thenReturn(new BigDecimal("100.00"));
    when(repo.sumCashOut()).thenReturn(new BigDecimal("40.00"));
    when(repo.sumProfit()).thenReturn(new BigDecimal("60.00"));

    GlobalStatsView stats = service.globalStats();

    assertEquals(10L, stats.getGames_count());
    assertEquals(3L, stats.getClient_count());
    assertEquals(new BigDecimal("100.00"), stats.getTotal_turnover());
    assertEquals(new BigDecimal("40.00"), stats.getTotal_cash_out());
    assertEquals(new BigDecimal("60.00"), stats.getTotal_profit());
  }

  @Test
  void userStats_returnsAggregatedValues() {
    long userId = 7L;

    when(repo.countByUserId(userId)).thenReturn(8L);
    when(repo.countByUserIdAndWinningTrue(userId)).thenReturn(3L);
    when(repo.sumTurnoverByUser(userId)).thenReturn(new BigDecimal("80.00"));
    when(repo.sumCashOutByUser(userId)).thenReturn(new BigDecimal("25.00"));
    when(repo.sumProfitByUser(userId)).thenReturn(new BigDecimal("55.00"));

    UserStatsView stats = service.userStats(userId);

    assertEquals(7L, stats.getUser());
    assertEquals(8L, stats.getGames_count());
    assertEquals(3L, stats.getWins());
    assertEquals(5L, stats.getLosses());
    assertEquals(new BigDecimal("80.00"), stats.getTurnover());
    assertEquals(new BigDecimal("25.00"), stats.getCash_out());
    assertEquals(new BigDecimal("55.00"), stats.getProfit());
  }
}
