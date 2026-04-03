package de.casino.roulette.service;

import de.casino.roulette.model.dto.BetType;
import de.casino.roulette.model.dto.PlayRequest;
import de.casino.roulette.model.dto.PlayResponse;
import de.casino.roulette.model.entity.RouletteGameEntity;
import de.casino.roulette.repository.RouletteGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RouletteGameServiceTest {

  private RouletteGameRepository repo;
  private RouletteGameService service;

  @BeforeEach
  void setUp() {
    repo = mock(RouletteGameRepository.class);
    service = new RouletteGameService(repo);
  }

  @Test
  void calculateAmount_numberWin_returns35xBet() {
    BigDecimal result = RouletteGameService.calculateAmount(BetType.NUMBER, new BigDecimal("10.00"), true);
    assertEquals(new BigDecimal("350.00"), result);
  }

  @Test
  void calculateAmount_colorWin_returns1xBet() {
    BigDecimal result = RouletteGameService.calculateAmount(BetType.COLOR, new BigDecimal("10.00"), true);
    assertEquals(new BigDecimal("10.00"), result);
  }

  @Test
  void calculateAmount_loss_returnsNegativeBet() {
    BigDecimal result = RouletteGameService.calculateAmount(BetType.COLOR, new BigDecimal("10.00"), false);
    assertEquals(new BigDecimal("-10.00"), result);
  }

  @Test
  void isWinning_number_returnsTrueForExactMatch() {
    boolean result = RouletteGameService.isWinning(BetType.NUMBER, "17", 17);
    assertTrue(result);
  }

  @Test
  void isWinning_number_returnsFalseForDifferentNumber() {
    boolean result = RouletteGameService.isWinning(BetType.NUMBER, "17", 12);
    assertFalse(result);
  }

  @Test
  void isWinning_color_redWinsOnRedNumber() {
    boolean result = RouletteGameService.isWinning(BetType.COLOR, "RED", 1);
    assertTrue(result);
  }

  @Test
  void isWinning_color_blackLosesOnRedNumber() {
    boolean result = RouletteGameService.isWinning(BetType.COLOR, "BLACK", 1);
    assertFalse(result);
  }

  @Test
  void isWinning_evenOdd_evenWinsOnEvenNumber() {
    boolean result = RouletteGameService.isWinning(BetType.EVEN_ODD, "EVEN", 8);
    assertTrue(result);
  }

  @Test
  void isWinning_highLow_lowWinsOnLowNumber() {
    boolean result = RouletteGameService.isWinning(BetType.HIGH_LOW, "LOW", 10);
    assertTrue(result);
  }

  @Test
  void isWinning_zeroLosesForColorEvenOddHighLow() {
    assertFalse(RouletteGameService.isWinning(BetType.COLOR, "RED", 0));
    assertFalse(RouletteGameService.isWinning(BetType.EVEN_ODD, "EVEN", 0));
    assertFalse(RouletteGameService.isWinning(BetType.HIGH_LOW, "LOW", 0));
  }

  @Test
  void isWinning_invalidNumberThrowsException() {
    IllegalArgumentException ex = assertThrows(
      IllegalArgumentException.class,
      () -> RouletteGameService.isWinning(BetType.NUMBER, "99", 17)
    );

    assertTrue(ex.getMessage().contains("0..36") || ex.getMessage().contains("range"));
  }

  @Test
  void isWinning_invalidColorThrowsException() {
    assertThrows(
      IllegalArgumentException.class,
      () -> RouletteGameService.isWinning(BetType.COLOR, "BLUE", 10)
    );
  }

  @Test
  void play_savesGameAndReturnsResponse() {
    PlayRequest req = new PlayRequest();
    req.setUser(1L);
    req.setBetAmount(new BigDecimal("10.00"));
    req.setBetType(BetType.COLOR);
    req.setBetValue("RED");

    when(repo.save(any(RouletteGameEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

    PlayResponse response = service.play(req);

    assertNotNull(response);
    assertEquals(1L, response.getUser());
    assertEquals(BetType.COLOR, response.getBetType());
    assertEquals("RED", response.getBetValue());
    assertEquals(new BigDecimal("10.00"), response.getBetAmount());
    assertTrue(response.getBall_position() >= 0 && response.getBall_position() <= 36);

    verify(repo, times(1)).save(any(RouletteGameEntity.class));

    ArgumentCaptor<RouletteGameEntity> captor = ArgumentCaptor.forClass(RouletteGameEntity.class);
    verify(repo).save(captor.capture());

    RouletteGameEntity saved = captor.getValue();
    assertEquals(1L, saved.getUserId());
    assertEquals(BetType.COLOR, saved.getBetType());
    assertEquals("RED", saved.getBetValue());
    assertEquals(new BigDecimal("10.00"), saved.getBetAmount());
    assertNotNull(saved.getCreatedAt());
    assertTrue(saved.getBallPosition() >= 0 && saved.getBallPosition() <= 36);
  }
}
