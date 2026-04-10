package de.casino.roulette.controller;

import de.casino.roulette.service.IRouletteInfoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/casino/roulette/api/info")
public class RouletteInfoController {

  private final IRouletteInfoService service;

  public RouletteInfoController(IRouletteInfoService service) {
    this.service = service;
  }

  @GetMapping("/rules")
  public String rules() {
    return service.rules();
  }

  @GetMapping("/chances")
  public String chances() {
    return service.chances();
  }
}