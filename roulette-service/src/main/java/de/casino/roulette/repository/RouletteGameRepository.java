package de.casino.roulette.repository;

import de.casino.roulette.model.entity.RouletteGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface RouletteGameRepository extends JpaRepository<RouletteGameEntity, Long> {

  List<RouletteGameEntity> findAllByUserIdOrderByCreatedAtDesc(Long userId);

  @Query("select count(distinct g.userId) from RouletteGameEntity g")
  long countDistinctUsers();

  @Query("select coalesce(sum(g.betAmount), 0) from RouletteGameEntity g")
  BigDecimal sumTurnover();

  @Query("select coalesce(sum(case when g.amount > 0 then g.amount else 0 end), 0) from RouletteGameEntity g")
  BigDecimal sumCashOut();

  @Query("select coalesce(sum(g.betAmount), 0) - coalesce(sum(case when g.amount > 0 then g.amount else 0 end), 0) from RouletteGameEntity g")
  BigDecimal sumProfit();

  @Query("select coalesce(sum(g.betAmount), 0) from RouletteGameEntity g where g.userId = :userId")
  BigDecimal sumTurnoverByUser(Long userId);

  @Query("select coalesce(sum(case when g.amount > 0 then g.amount else 0 end), 0) from RouletteGameEntity g where g.userId = :userId")
  BigDecimal sumCashOutByUser(Long userId);

  @Query("select coalesce(sum(g.betAmount), 0) - coalesce(sum(case when g.amount > 0 then g.amount else 0 end), 0) from RouletteGameEntity g where g.userId = :userId")
  BigDecimal sumProfitByUser(Long userId);

  long countByUserId(Long userId);
  long countByUserIdAndWinningTrue(Long userId);
}
