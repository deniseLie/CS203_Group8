package csd.backend.Account.MS.repository.Tournament;

import org.springframework.data.jpa.repository.JpaRepository;

import csd.backend.Account.MS.model.Tournament.TournamentPlayerStats;

import java.util.List;

public interface TournamentPlayerStatsRepository extends JpaRepository<TournamentPlayerStats, Long> {

    // Find all stats for a player
    List<TournamentPlayerStats> findByPlayerId(Long playerId);

    // Find all stats for a tournament
    List<TournamentPlayerStats> findByTournamentTournamentId(Long tournamentId);
}
