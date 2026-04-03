package de.casino.roulette.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HealthControllerTest {

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    HealthController controller = new HealthController();
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void health_returns200() throws Exception {
    mockMvc.perform(get("/casino/roulette/api/health"))
      .andExpect(status().isOk())
      .andExpect(content().string("ok"));
  }
}
