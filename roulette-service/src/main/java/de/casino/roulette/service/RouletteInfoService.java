package de.casino.roulette.service;

import org.springframework.stereotype.Service;

@Service
public class RouletteInfoService implements IRouletteInfoService {

    @Override
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

    @Override
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