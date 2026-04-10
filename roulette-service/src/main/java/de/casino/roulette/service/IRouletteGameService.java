package de.casino.roulette.service;

import de.casino.roulette.model.dto.PlayRequest;
import de.casino.roulette.model.dto.PlayResponse;

public interface IRouletteGameService {
    PlayResponse play(PlayRequest req);
}