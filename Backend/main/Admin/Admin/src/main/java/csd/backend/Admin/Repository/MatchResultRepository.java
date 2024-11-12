package csd.backend.Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import csd.backend.Admin.Model.MatchResult;

public interface MatchResultRepository extends JpaRepository<MatchResult, Integer> {
    // Custom method to find by match result ID (if needed)
    Optional<MatchResult> findByTournamentId(int tournamentId);
}

