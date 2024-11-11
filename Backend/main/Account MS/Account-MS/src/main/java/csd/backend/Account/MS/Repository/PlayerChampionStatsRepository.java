package csd.backend.Account.MS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import csd.backend.Account.MS.Model.*;

public interface PlayerChampionStatsRepository extends JpaRepository<PlayerChampionStats, Long>{
    PlayerChampionStats findByPlayerIdAndChampionId(Long playerId, int championId);
}
