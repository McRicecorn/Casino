package de.casino.roulette.controller;

import de.casino.roulette.model.dto.BetType;
import de.casino.roulette.model.dto.PlayResponse;
import de.casino.roulette.service.IRouletteGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RoulettePlayControllerTest {

  private MockMvc mockMvc;
  private IRouletteGameService service;

  @BeforeEach
  void setUp() {
    service = mock(IRouletteGameService.class);

    RoulettePlayController controller = new RoulettePlayController(service);
    ApiExceptionHandler exceptionHandler = new ApiExceptionHandler();

    mockMvc = MockMvcBuilders.standaloneSetup(controller)
      .setControllerAdvice(exceptionHandler)
      .build();
  }

  @Test
  void play_returns200AndResponseBody() throws Exception {
    PlayResponse response = new PlayResponse(
      1L,
      false,
      new BigDecimal("-10.00"),
      33,
      BetType.COLOR,
      "RED",
      new BigDecimal("10.00")
    );

    when(service.play(any())).thenReturn(response);

    String json = """
                {
                  "user": 1,
                  "betAmount": 10.00,
                  "betType": "COLOR",
                  "betValue": "RED"
                }
                """;

    mockMvc.perform(post("/casino/roulette/api/play")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.user").value(1))
      .andExpect(jsonPath("$.winning").value(false))
      .andExpect(jsonPath("$.amount").value(-10.00))
      .andExpect(jsonPath("$.betType").value("COLOR"))
      .andExpect(jsonPath("$.betValue").value("RED"))
      .andExpect(jsonPath("$.ball_position").value(33));
  }

  @Test
  void play_returns400ForInvalidRequestBody() throws Exception {
    String json = """
                {
                  "user": 1,
                  "betAmount": 0,
                  "betType": "COLOR",
                  "betValue": ""
                }
                """;

    mockMvc.perform(post("/casino/roulette/api/play")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
      .andExpect(status().isBadRequest());
  }
}
