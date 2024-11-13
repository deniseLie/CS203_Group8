package csd.backend.Admin.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import csd.backend.Admin.Model.Tournament.Tournament;
import csd.backend.Admin.Model.Tournament.TournamentRound;

public interface TournamentRoundRepository extends JpaRepository<TournamentRound, Long>{
    // Find the latest round by tournament (highest round number)
    Optional<TournamentRound> findTopByTournamentOrderByRoundNumberDesc(Tournament tournament);
}