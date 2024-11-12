package csd.backend.Admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResult, Integer> {
    // Custom method to find by match result ID (if needed)
    MatchResult findByTournamentId(int tournamentId);
}

