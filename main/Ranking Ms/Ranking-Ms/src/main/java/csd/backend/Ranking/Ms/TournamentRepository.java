package csd.backend.Ranking.Ms;

// public class TournamentRepository {
    
// }

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    
}
