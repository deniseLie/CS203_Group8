package csd.backend.Account.MS.Repository.Player;

import org.springframework.data.jpa.repository.JpaRepository;
import csd.backend.Account.MS.Model.Player.*;

public interface PlayerOverallStatsRepository extends JpaRepository<PlayerOverallStats, Long> {
    PlayerOverallStats findByPlayerId(Long playerId);  // Query to get stats by playerId
    Long findRankIdByPlayerId(Long playerId);
}
