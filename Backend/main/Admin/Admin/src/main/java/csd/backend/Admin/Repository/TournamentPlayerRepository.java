package csd.backend.Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import csd.backend.Admin.Model.Tournament.Tournament;
import csd.backend.Admin.Model.Tournament.TournamentPlayer;
import java.util.*;

public interface TournamentPlayerRepository extends JpaRepository<TournamentPlayer, Long>{
    Optional<TournamentPlayer> findByPlayerId(Long playerId);
    List<TournamentPlayer> findByTournament(Tournament tournament);
    Optional<TournamentPlayer> findByPlayerIdAndTournament_TournamentId(Long playerId, Long tournamentId);
}
