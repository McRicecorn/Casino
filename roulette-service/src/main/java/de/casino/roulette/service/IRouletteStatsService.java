package de.casino.roulette.service;

import de.casino.roulette.model.dto.GlobalStatsView;
import de.casino.roulette.model.dto.UserStatsView;

public interface IRouletteStatsService {
    GlobalStatsView globalStats();
    UserStatsView userStats(long userId);
}