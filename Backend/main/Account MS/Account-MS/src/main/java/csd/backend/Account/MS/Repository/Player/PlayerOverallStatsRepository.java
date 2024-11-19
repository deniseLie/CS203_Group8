package csd.backend.Account.MS.repository.Player;

import org.springframework.data.jpa.repository.JpaRepository;

import csd.backend.Account.MS.model.Player.*;

public interface PlayerOverallStatsRepository extends JpaRepository<PlayerOverallStats, Long> {
    PlayerOverallStats findByPlayerId(Long playerId);  // Query to get stats by playerId
    Long findRankIdByPlayerId(Long playerId);
}
