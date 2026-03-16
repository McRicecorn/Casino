package de.casino.roulette.service;

import de.casino.roulette.model.dto.GlobalStatsView;
import de.casino.roulette.model.dto.UserStatsView;
import de.casino.roulette.repository.RouletteGameRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RouletteStatsService {

  private final RouletteGameRepository repo;

  public RouletteStatsService(RouletteGameRepository repo) {
    this.repo = repo;
  }

  public GlobalStatsView globalStats() {
    long games = repo.count();
    long clients = repo.countDistinctUsers();
    BigDecimal turnover = repo.sumTurnover();
    BigDecimal cashOut = repo.sumCashOut();
    BigDecimal profit = repo.sumProfit();
    return new GlobalStatsView(games, clients, turnover, cashOut, profit);
  }

  public UserStatsView userStats(long userId) {
    long games = repo.countByUserId(userId);
    long wins = repo.countByUserIdAndWinningTrue(userId);
    long losses = games - wins;

    BigDecimal turnover = repo.sumTurnoverByUser(userId);
    BigDecimal cashOut = repo.sumCashOutByUser(userId);
    BigDecimal profit = repo.sumProfitByUser(userId);

    return new UserStatsView(userId, games, wins, losses, turnover, cashOut, profit);
  }
}
