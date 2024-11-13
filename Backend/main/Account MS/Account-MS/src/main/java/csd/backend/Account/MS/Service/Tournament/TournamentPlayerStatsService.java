package csd.backend.Account.MS.Service.Tournament;

import csd.backend.Account.MS.Model.Tournament.*;
import csd.backend.Account.MS.Repository.Tournament.*;

public class TournamentPlayerStatsService {

    TournamentPlayerStatsRepository tournamentPlayerStatsRepository; 

    public TournamentPlayerStatsService(TournamentPlayerStatsRepository tournamentPlayerStatsRepository) {
        this.tournamentPlayerStatsRepository = tournamentPlayerStatsRepository;
    }

    public void savePlayerStats(TournamentPlayerStats playerStats) {
        tournamentPlayerStatsRepository.save(playerStats);
    }

}
