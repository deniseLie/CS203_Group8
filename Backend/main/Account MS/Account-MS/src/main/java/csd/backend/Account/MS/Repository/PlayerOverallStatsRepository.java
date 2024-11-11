package csd.backend.Account.MS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import csd.backend.Account.MS.Model.*;

public interface PlayerOverallStatsRepository extends JpaRepository<PlayerOverallStats, Long> {
    PlayerOverallStats findByPlayerId(Long playerId);  // Query to get stats by playerId
}
