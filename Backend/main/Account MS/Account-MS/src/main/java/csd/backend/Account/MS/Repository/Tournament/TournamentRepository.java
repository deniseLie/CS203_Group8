package csd.backend.Account.MS.Repository.Tournament;

import org.springframework.data.jpa.repository.JpaRepository;
import csd.backend.Account.MS.Model.Tournament.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Long>{
    
}
