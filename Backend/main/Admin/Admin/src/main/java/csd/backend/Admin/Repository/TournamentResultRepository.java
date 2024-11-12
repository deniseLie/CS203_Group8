package csd.backend.Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import csd.backend.Admin.Model.TournamentResult;

public interface TournamentResultRepository extends JpaRepository<TournamentResult, Integer> {
    // Custom method to find by tournament result ID (if needed)
    Optional<TournamentResult> findByTournamentId(int tournamentId);
}

