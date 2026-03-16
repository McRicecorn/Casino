package de.casino.roulette.controller;

import de.casino.roulette.model.dto.GameView;
import de.casino.roulette.model.entity.RouletteGameEntity;
import de.casino.roulette.repository.RouletteGameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/casino/roulette/api")
public class RouletteGameController {

  private final RouletteGameRepository repo;

  public RouletteGameController(RouletteGameRepository repo) {
    this.repo = repo;
  }

  @GetMapping("/stats/games")
  public List<GameView> allGames() {
    return repo.findAll().stream().map(this::toView).toList();
  }

  @GetMapping("/stat/{gameId}")
  public ResponseEntity<GameView> one(@PathVariable long gameId) {
    return repo.findById(gameId)
      .map(e -> ResponseEntity.ok(toView(e)))
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/stat/{gameId}")
  public ResponseEntity<Void> delete(@PathVariable long gameId) {
    if (!repo.existsById(gameId)) return ResponseEntity.notFound().build();
    repo.deleteById(gameId);
    return ResponseEntity.ok().build();
  }

  private GameView toView(RouletteGameEntity e) {
    return new GameView(
      e.getId(),
      e.getUserId(),
      e.getBetType(),
      e.getBetValue(),
      e.getBetAmount(),
      e.getBallPosition(),
      e.isWinning(),
      e.getAmount(),
      e.getCreatedAt()
    );
  }
}
