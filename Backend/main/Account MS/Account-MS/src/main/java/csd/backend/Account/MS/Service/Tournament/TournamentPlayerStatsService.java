package csd.backend.Account.MS.service.tournament;

import csd.backend.Account.MS.model.tournament.*;
import csd.backend.Account.MS.repository.tournament.*;

public class TournamentPlayerStatsService {

    TournamentPlayerStatsRepository tournamentPlayerStatsRepository; 

    public TournamentPlayerStatsService(TournamentPlayerStatsRepository tournamentPlayerStatsRepository) {
        this.tournamentPlayerStatsRepository = tournamentPlayerStatsRepository;
    }

    public void savePlayerStats(TournamentPlayerStats playerStats) {
        tournamentPlayerStatsRepository.save(playerStats);
    }

}
