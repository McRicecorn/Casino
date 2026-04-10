package de.casino.roulette.controller;

import de.casino.roulette.model.dto.GlobalStatsView;
import de.casino.roulette.model.dto.UserStatsView;
import de.casino.roulette.service.IRouletteStatsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/casino/roulette/api/stats")
public class RouletteStatsController {

  private final IRouletteStatsService stats;

  public RouletteStatsController(IRouletteStatsService stats) {
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
