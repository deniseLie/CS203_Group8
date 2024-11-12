package csd.backend.Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import csd.backend.Admin.Model.Match;

public interface MatchRepository extends JpaRepository<Match, Integer> {
    
}
