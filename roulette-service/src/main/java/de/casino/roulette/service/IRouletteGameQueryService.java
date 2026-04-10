package de.casino.roulette.service;

import de.casino.roulette.model.dto.GameView;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IRouletteGameQueryService {
    List<GameView> allGames();
    ResponseEntity<GameView> one(long gameId);
    ResponseEntity<Void> delete(long gameId);
}