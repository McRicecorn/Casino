package de.casino.roulette.controller;

import de.casino.roulette.model.dto.PlayRequest;
import de.casino.roulette.model.dto.PlayResponse;
import de.casino.roulette.service.IRouletteGameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/casino/roulette/api")
public class RoulettePlayController {

  private final IRouletteGameService service;

  public RoulettePlayController(IRouletteGameService service) {
    this.service = service;
  }

  @PostMapping("/play")
  public ResponseEntity<PlayResponse> play(@Valid @RequestBody PlayRequest request) {
    return ResponseEntity.ok(service.play(request));
  }
}
