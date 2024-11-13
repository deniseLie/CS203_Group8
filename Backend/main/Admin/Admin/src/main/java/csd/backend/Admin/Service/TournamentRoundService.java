package csd.backend.Admin.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csd.backend.Admin.Model.Tournament.*;
import csd.backend.Admin.Model.User.*;
import csd.backend.Admin.Repository.*;
import csd.backend.Admin.Service.User.*;

@Service
public class TournamentRoundService {

    private final TournamentService tournamentService;
    private final PlayerService playerService;
    private final TournamentPlayerService tournamentPlayerService;
    private final TournamentRoundRepository tournamentRoundRepository;
    
    @Autowired
    public TournamentRoundService(
        TournamentService tournamentService, PlayerService playerService, TournamentPlayerService tournamentPlayerService, TournamentRoundRepository tournamentRoundRepository
    ) {
        this.tournamentService = tournamentService;
        this.playerService = playerService;
        this.tournamentPlayerService = tournamentPlayerService;
        this.tournamentRoundRepository = tournamentRoundRepository;
    }

    // Method to create or update a tournament round
    public String createOrUpdateTournamentRound(Long tournamentId, Long firstPlayerId, Long secondPlayerId, Long winnerPlayerId, int roundNumber) {
        try {
            // Find the tournament
            Tournament tournament = tournamentService.getTournamentById(tournamentId);
            if (tournament == null) {
                return "Tournament not found";
            }

            // Find the players
            Player firstPlayer = tournamentPlayerService.getPlayerById(firstPlayerId);
            Player secondPlayer = tournamentPlayerService.getPlayerById(secondPlayerId);
            Player winnerPlayer = tournamentPlayerService.getPlayerById(winnerPlayerId);

            if (firstPlayer == null || secondPlayer == null || winnerPlayer == null) {
                return "One or more players not found";
            }

            // Create or update the TournamentRound
            TournamentRound round = new TournamentRound();
            round.setTournament(tournament);
            round.setFirstPlayer(firstPlayer);
            round.setSecondPlayer(secondPlayer);
            round.setRoundNumber(roundNumber);
            round.setWinnerPlayer(winnerPlayer);

            // Save the round to the database
            tournamentRoundRepository.save(round);

            return "Tournament round updated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing tournament round: " + e.getMessage();
        }
    }
}
