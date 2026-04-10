package de.casino.roulette.service;

import de.casino.roulette.model.dto.GameView;
import de.casino.roulette.model.entity.RouletteGameEntity;
import de.casino.roulette.repository.RouletteGameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouletteGameQueryService implements IRouletteGameQueryService {

    private final RouletteGameRepository repo;

    public RouletteGameQueryService(RouletteGameRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<GameView> allGames() {
        return repo.findAll().stream().map(this::toView).toList();
    }

    @Override
    public ResponseEntity<GameView> one(long gameId) {
        return repo.findById(gameId)
                .map(e -> ResponseEntity.ok(toView(e)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> delete(long gameId) {
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