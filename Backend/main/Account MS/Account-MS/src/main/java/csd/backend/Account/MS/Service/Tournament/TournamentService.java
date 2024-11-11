package csd.backend.Account.MS.Service.Tournament;

import csd.backend.Account.MS.Model.Tournament.*;
import csd.backend.Account.MS.Repository.Tournament.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TournamentPlayerStatsRepository tournamentPlayerStatsRepository;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository, TournamentPlayerStatsRepository tournamentPlayerStatsRepository) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentPlayerStatsRepository = tournamentPlayerStatsRepository;
    }

    // Get all tournaments
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    // Get tournament by ID
    public Tournament getTournamentById(Long tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found with ID: " + tournamentId));
    }

    // Get all player stats for a tournament
    public List<TournamentPlayerStats> getTournamentPlayerStats(Long tournamentId) {
        return tournamentPlayerStatsRepository.findByTournamentTournamentId(tournamentId);
    }

    // Get tournament player stats for a player
    public List<TournamentPlayerStats> getTournamentPlayerStatsForPlayer(Long playerId) {
        return tournamentPlayerStatsRepository.findByPlayerId(playerId);
    }
}
