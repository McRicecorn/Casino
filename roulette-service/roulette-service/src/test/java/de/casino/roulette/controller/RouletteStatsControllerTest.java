package de.casino.roulette.controller;

import de.casino.roulette.model.dto.GlobalStatsView;
import de.casino.roulette.model.dto.UserStatsView;
import de.casino.roulette.service.RouletteStatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RouletteStatsControllerTest {

  private MockMvc mockMvc;
  private RouletteStatsService statsService;

  @BeforeEach
  void setUp() {
    statsService = mock(RouletteStatsService.class);
    RouletteStatsController controller = new RouletteStatsController(statsService);

    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void globalStats_returns200AndJson() throws Exception {
    GlobalStatsView stats = new GlobalStatsView(
      12L,
      4L,
      new BigDecimal("120.00"),
      new BigDecimal("50.00"),
      new BigDecimal("70.00")
    );

    when(statsService.globalStats()).thenReturn(stats);

    mockMvc.perform(get("/casino/roulette/api/stats"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.games_count").value(12))
      .andExpect(jsonPath("$.client_count").value(4))
      .andExpect(jsonPath("$.total_turnover").value(120.00))
      .andExpect(jsonPath("$.total_cash_out").value(50.00))
      .andExpect(jsonPath("$.total_profit").value(70.00));
  }

  @Test
  void userStats_returns200AndJson() throws Exception {
    UserStatsView stats = new UserStatsView(
      7L,
      8L,
      3L,
      5L,
      new BigDecimal("80.00"),
      new BigDecimal("25.00"),
      new BigDecimal("55.00")
    );

    when(statsService.userStats(7L)).thenReturn(stats);

    mockMvc.perform(get("/casino/roulette/api/stats/user/7"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.user").value(7))
      .andExpect(jsonPath("$.games_count").value(8))
      .andExpect(jsonPath("$.wins").value(3))
      .andExpect(jsonPath("$.losses").value(5))
      .andExpect(jsonPath("$.turnover").value(80.00))
      .andExpect(jsonPath("$.cash_out").value(25.00))
      .andExpect(jsonPath("$.profit").value(55.00));
  }
}
