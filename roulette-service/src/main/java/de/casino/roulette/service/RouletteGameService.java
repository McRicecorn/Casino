package de.casino.roulette.service;

import de.casino.roulette.model.dto.*;
import de.casino.roulette.model.entity.RouletteGameEntity;
import de.casino.roulette.repository.RouletteGameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Locale;

@Service
public class RouletteGameService implements IRouletteGameService {

  private final RouletteGameRepository repo;
  private final SecureRandom random = new SecureRandom();

  public RouletteGameService(RouletteGameRepository repo) {
    this.repo = repo;
  }
  @Override
  @Transactional
  public PlayResponse play(PlayRequest req) {
    int ball = random.nextInt(37);

    boolean win = isWinning(req.getBetType(), req.getBetValue(), ball);
    BigDecimal amount = calculateAmount(req.getBetType(), req.getBetAmount(), win);

    RouletteGameEntity saved = repo.save(new RouletteGameEntity(
      req.getUser(),
      req.getBetType(),
      normalize(req.getBetValue()),
      req.getBetAmount(),
      ball,
      win,
      amount,
      Instant.now()
    ));

    return new PlayResponse(
      req.getUser(),
      win,
      amount,
      ball,
      req.getBetType(),
      normalize(req.getBetValue()),
      req.getBetAmount()
    );
  }

  static String normalize(String v) {
    return v == null ? null : v.trim().toUpperCase(Locale.ROOT);
  }

  static BigDecimal calculateAmount(BetType type, BigDecimal betAmount, boolean win) {
    if (!win) return betAmount.negate();

    return switch (type) {
      case NUMBER -> betAmount.multiply(BigDecimal.valueOf(35));
      case COLOR, EVEN_ODD, HIGH_LOW -> betAmount; // 1x payout
    };
  }

  static boolean isWinning(BetType type, String betValueRaw, int ball) {
    String betValue = normalize(betValueRaw);

    return switch (type) {
      case NUMBER -> {
        int n;
        try { n = Integer.parseInt(betValue); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("betValue must be a number 0..36"); }
        if (n < 0 || n > 36) throw new IllegalArgumentException("betValue must be in range 0..36");
        yield ball == n;
      }
      case COLOR -> {
        if (ball == 0) yield false;
        if (!betValue.equals("RED") && !betValue.equals("BLACK"))
          throw new IllegalArgumentException("betValue must be RED or BLACK");
        yield betValue.equals(colorOf(ball));
      }
      case EVEN_ODD -> {
        if (ball == 0) yield false;
        if (!betValue.equals("EVEN") && !betValue.equals("ODD"))
          throw new IllegalArgumentException("betValue must be EVEN or ODD");
        boolean even = (ball % 2 == 0);
        yield (betValue.equals("EVEN") && even) || (betValue.equals("ODD") && !even);
      }
      case HIGH_LOW -> {
        if (ball == 0) yield false;
        if (!betValue.equals("LOW") && !betValue.equals("HIGH"))
          throw new IllegalArgumentException("betValue must be LOW or HIGH");
        boolean low = (ball >= 1 && ball <= 18);
        yield (betValue.equals("LOW") && low) || (betValue.equals("HIGH") && !low);
      }
    };
  }

  // European roulette red numbers
  static String colorOf(int n) {
    int[] reds = {1,3,5,7,9,12,14,16,18,19,21,23,25,27,30,32,34,36};
    for (int r : reds) if (r == n) return "RED";
    return "BLACK";
  }
}
