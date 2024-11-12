package csd.backend.Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import csd.backend.Admin.Model.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    
}
