package de.casino.roulette.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RouletteInfoControllerTest {

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    RouletteInfoController controller = new RouletteInfoController();
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void rules_returns200AndText() throws Exception {
    mockMvc.perform(get("/casino/roulette/api/info/rules"))
      .andExpect(status().isOk())
      .andExpect(content().string(org.hamcrest.Matchers.containsString("European Roulette")));
  }

  @Test
  void chances_returns200AndText() throws Exception {
    mockMvc.perform(get("/casino/roulette/api/info/chances"))
      .andExpect(status().isOk())
      .andExpect(content().string(org.hamcrest.Matchers.containsString("37 slots")));
  }
}
