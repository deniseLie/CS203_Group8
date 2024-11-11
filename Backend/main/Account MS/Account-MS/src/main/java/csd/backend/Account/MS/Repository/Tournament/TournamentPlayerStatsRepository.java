package csd.backend.Account.MS.Repository.Tournament;

import org.springframework.data.jpa.repository.JpaRepository;
import csd.backend.Account.MS.Model.Tournament.TournamentPlayerStats;

public interface TournamentPlayerStatsRepository extends JpaRepository<TournamentPlayerStats, Long>{
    
}
