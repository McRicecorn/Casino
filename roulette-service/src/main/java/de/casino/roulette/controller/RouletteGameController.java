package de.casino.roulette.controller;

import de.casino.roulette.model.dto.GameView;
import de.casino.roulette.service.IRouletteGameQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/casino/roulette/api")
public class RouletteGameController {

  private final IRouletteGameQueryService service;

  public RouletteGameController(IRouletteGameQueryService service) {
    this.service = service;
  }

  @GetMapping("/stats/games")
  public List<GameView> allGames() {
    return service.allGames();
  }

  @GetMapping("/stat/{gameId}")
  public ResponseEntity<GameView> one(@PathVariable long gameId) {
    return service.one(gameId);
  }

  @DeleteMapping("/stat/{gameId}")
  public ResponseEntity<Void> delete(@PathVariable long gameId) {
    return service.delete(gameId);
  }
}