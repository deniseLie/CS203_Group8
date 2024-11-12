package csd.backend.Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import csd.backend.Admin.Model.Tournament.TournamentRound;

public interface TournamentRoundRepository extends JpaRepository<TournamentRound, Integer>{
    
}