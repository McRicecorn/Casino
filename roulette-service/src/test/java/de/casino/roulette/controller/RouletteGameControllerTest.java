package de.casino.roulette.controller;

import de.casino.roulette.model.dto.BetType;
import de.casino.roulette.model.dto.GameView;
import de.casino.roulette.service.IRouletteGameQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RouletteGameControllerTest {

  private MockMvc mockMvc;
  private IRouletteGameQueryService service;

  @BeforeEach
  void setUp() {
    service = mock(IRouletteGameQueryService.class);
    RouletteGameController controller = new RouletteGameController(service);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void allGames_returnsList() throws Exception {
    GameView game = new GameView(1L, 1L, BetType.COLOR, "RED", new BigDecimal("10.00"), 33, false, new BigDecimal("-10.00"), Instant.parse("2026-02-27T12:00:00Z")
    );

    when(service.allGames()).thenReturn(List.of(game));

    mockMvc.perform(get("/casino/roulette/api/stats/games"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].user").value(1))
            .andExpect(jsonPath("$[0].betType").value("COLOR"))
            .andExpect(jsonPath("$[0].betValue").value("RED"))
            .andExpect(jsonPath("$[0].betAmount").value(10.00))
            .andExpect(jsonPath("$[0].ball_position").value(33))
            .andExpect(jsonPath("$[0].winning").value(false))
            .andExpect(jsonPath("$[0].amount").value(-10.00));
  }

  @Test
  void one_returnsGameWhenFound() throws Exception {
    GameView game = new GameView(1L, 1L, BetType.NUMBER, "17", new BigDecimal("5.00"), 17, true, new BigDecimal("175.00"), Instant.parse("2026-02-27T12:00:00Z")
    );

    when(service.one(1L)).thenReturn(ResponseEntity.ok(game));

    mockMvc.perform(get("/casino/roulette/api/stat/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.user").value(1))
            .andExpect(jsonPath("$.betType").value("NUMBER"))
            .andExpect(jsonPath("$.betValue").value("17"))
            .andExpect(jsonPath("$.betAmount").value(5.00))
            .andExpect(jsonPath("$.ball_position").value(17))
            .andExpect(jsonPath("$.winning").value(true))
            .andExpect(jsonPath("$.amount").value(175.00));
  }

  @Test
  void one_returns404WhenNotFound() throws Exception {
    when(service.one(99L)).thenReturn(ResponseEntity.notFound().build());

    mockMvc.perform(get("/casino/roulette/api/stat/99"))
      .andExpect(status().isNotFound());
  }

  @Test
  void delete_returns200WhenFound() throws Exception {
    when(service.delete(1L)).thenReturn(ResponseEntity.ok().build());

    mockMvc.perform(delete("/casino/roulette/api/stat/1"))
            .andExpect(status().isOk());

    verify(service).delete(1L);
  }

  @Test
  void delete_returns404WhenNotFound() throws Exception {
    when(service.delete(99L)).thenReturn(ResponseEntity.notFound().build());

    mockMvc.perform(delete("/casino/roulette/api/stat/99"))
            .andExpect(status().isNotFound());

    verify(service).delete(99L);
  }
}
