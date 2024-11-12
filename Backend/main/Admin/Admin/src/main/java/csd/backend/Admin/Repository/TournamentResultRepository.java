package csd.backend.Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import csd.backend.Admin.Model.Tournament.TournamentResult;

import java.util.Optional;

public interface TournamentResultRepository extends JpaRepository<TournamentResult, Integer> {
    // Custom method to find by tournament result ID (if needed)
    Optional<TournamentResult> findByTournamentId(int tournamentId);
}

