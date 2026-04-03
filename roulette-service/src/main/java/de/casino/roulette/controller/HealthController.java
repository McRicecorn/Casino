package de.casino.roulette.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

  @GetMapping("/casino/roulette/api/health")
  public String health() {
    return "ok";
  }
}
