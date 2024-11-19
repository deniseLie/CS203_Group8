package csd.backend.Account.MS.repository.Player;

import org.springframework.data.jpa.repository.JpaRepository;

import csd.backend.Account.MS.model.Player.*;

import java.util.*;

public interface PlayerChampionStatsRepository extends JpaRepository<PlayerChampionStats, Long>{
    PlayerChampionStats findByPlayerIdAndChampionId(Long playerId, Long championId);
    List<PlayerChampionStats> findByPlayerId(Long playerId);
}
