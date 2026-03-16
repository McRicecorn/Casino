package de.casino.roulette.controller;

import de.casino.roulette.model.dto.GlobalStatsView;
import de.casino.roulette.model.dto.UserStatsView;
import de.casino.roulette.service.RouletteStatsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/casino/roulette/api/stats")
public class RouletteStatsController {

  private final RouletteStatsService stats;

  public RouletteStatsController(RouletteStatsService stats) {
    this.stats = stats;
  }

  @GetMapping
  public GlobalStatsView global() {
    return stats.globalStats();
  }

  @GetMapping("/user/{userId}")
  public UserStatsView user(@PathVariable long userId) {
    return stats.userStats(userId);
  }
}
