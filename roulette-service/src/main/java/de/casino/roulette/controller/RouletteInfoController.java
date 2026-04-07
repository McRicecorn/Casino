package de.casino.roulette.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/casino/roulette/api/info")
public class RouletteInfoController {

  @GetMapping("/rules")
  public String rules() {
    return """
               European Roulette (0-36).
               Bet types supported:
               - NUMBER: exact number 0..36 (payout 35x)
               - COLOR: RED/BLACK (payout 1x, 0 loses)
               - EVEN_ODD: EVEN/ODD (payout 1x, 0 loses)
               - HIGH_LOW: LOW(1-18)/HIGH(19-36) (payout 1x, 0 loses)
               """;
  }

  @GetMapping("/chances")
  public String chances() {
    return """
               Chances (European Roulette, 37 slots):
               - NUMBER: 1/37 win probability, payout 35x
               - COLOR: 18/37 win probability (0 loses), payout 1x
               - EVEN/ODD: 18/37 win probability (0 loses), payout 1x
               - HIGH/LOW: 18/37 win probability (0 loses), payout 1x
               """;
  }
}
