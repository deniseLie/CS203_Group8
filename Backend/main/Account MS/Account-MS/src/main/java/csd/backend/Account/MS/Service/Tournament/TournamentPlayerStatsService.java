package csd.backend.Account.MS.service.Tournament;

import csd.backend.Account.MS.model.Tournament.*;
import csd.backend.Account.MS.repository.Tournament.*;

public class TournamentPlayerStatsService {

    TournamentPlayerStatsRepository tournamentPlayerStatsRepository; 

    public TournamentPlayerStatsService(TournamentPlayerStatsRepository tournamentPlayerStatsRepository) {
        this.tournamentPlayerStatsRepository = tournamentPlayerStatsRepository;
    }

    public void savePlayerStats(TournamentPlayerStats playerStats) {
        tournamentPlayerStatsRepository.save(playerStats);
    }

}
