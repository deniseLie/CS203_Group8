package csd.backend.Ranking.MS;

// public class TournamentRepository {
    
// }

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    
}
