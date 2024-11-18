package csd.backend.Admin.Service;

import org.springframework.stereotype.Service;

import csd.backend.Admin.Model.Tournament.*;
import csd.backend.Admin.Model.User.*;
import csd.backend.Admin.Repository.*;

@Service
public class TournamentRoundService {

    private final TournamentService tournamentService;
    private final TournamentPlayerService tournamentPlayerService;
    private final TournamentRoundRepository tournamentRoundRepository;
    
    public TournamentRoundService(
        TournamentService tournamentService, TournamentPlayerService tournamentPlayerService, TournamentRoundRepository tournamentRoundRepository
    ) {
        this.tournamentService = tournamentService;
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

            // calculate loser elo when total tournament round != roundNumber 
                // else calculate both loser and winner elo 

            // return back ELO based on player id 

            // call sqs to call account service "addTournament"

            return "Tournament round updated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing tournament round: " + e.getMessage();
        }
    }

    // Call Account service new Tournament
}
