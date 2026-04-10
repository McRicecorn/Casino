package de.casino.roulette.service;

import de.casino.roulette.model.dto.BetType;
import de.casino.roulette.model.dto.GameView;
import de.casino.roulette.model.entity.RouletteGameEntity;
import de.casino.roulette.repository.RouletteGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RouletteGameQueryServiceTest {

    private RouletteGameRepository repo;
    private RouletteGameQueryService service;

    @BeforeEach
    void setUp() {
        repo = mock(RouletteGameRepository.class);
        service = new RouletteGameQueryService(repo);
    }

    @Test
    void allGames_returnsMappedViews() {
        RouletteGameEntity game = new RouletteGameEntity(
                1L,
                BetType.COLOR,
                "RED",
                new BigDecimal("10.00"),
                33,
                false,
                new BigDecimal("-10.00"),
                Instant.parse("2026-02-27T12:00:00Z")
        );
        game.setId(1L);

        when(repo.findAll()).thenReturn(List.of(game));

        List<GameView> result = service.allGames();

        assertEquals(1, result.size());

        GameView view = result.get(0);
        assertEquals(1L, view.getId());
        assertEquals(1L, view.getUser());
        assertEquals(BetType.COLOR, view.getBetType());
        assertEquals("RED", view.getBetValue());
        assertEquals(new BigDecimal("10.00"), view.getBetAmount());
        assertEquals(33, view.getBall_position());
        assertFalse(view.isWinning());
        assertEquals(new BigDecimal("-10.00"), view.getAmount());
        assertEquals(Instant.parse("2026-02-27T12:00:00Z"), view.getCreatedAt());
    }

    @Test
    void one_returnsOkWhenGameExists() {
        RouletteGameEntity game = new RouletteGameEntity(
                1L,
                BetType.NUMBER,
                "17",
                new BigDecimal("5.00"),
                17,
                true,
                new BigDecimal("175.00"),
                Instant.parse("2026-02-27T12:00:00Z")
        );
        game.setId(1L);

        when(repo.findById(1L)).thenReturn(Optional.of(game));

        ResponseEntity<GameView> response = service.one(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("17", response.getBody().getBetValue());
    }

    @Test
    void one_returns404WhenGameDoesNotExist() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<GameView> response = service.one(99L);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void delete_returnsOkWhenGameExists() {
        when(repo.existsById(1L)).thenReturn(true);

        ResponseEntity<Void> response = service.delete(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(repo).deleteById(1L);
    }

    @Test
    void delete_returns404WhenGameDoesNotExist() {
        when(repo.existsById(99L)).thenReturn(false);

        ResponseEntity<Void> response = service.delete(99L);

        assertEquals(404, response.getStatusCode().value());
        verify(repo, never()).deleteById(99L);
    }
}