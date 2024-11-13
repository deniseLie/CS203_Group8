package csd.backend.Admin.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csd.backend.Admin.Model.Tournament.*;
import csd.backend.Admin.Model.User.*;
import csd.backend.Admin.Service.User.*;
import csd.backend.Admin.Repository.*;

@Service
public class TournamentPlayerService {

    private final TournamentPlayerRepository tournamentPlayerRepository;
    private final TournamentService tournamentService;
    private final PlayerService playerService;

    @Autowired
    public TournamentPlayerService(TournamentPlayerRepository tournamentPlayerRepository,
                                   TournamentService tournamentService,
                                   PlayerService playerService
    ) {
        this.tournamentPlayerRepository = tournamentPlayerRepository;
        this.tournamentService = tournamentService;
        this.playerService = playerService;
    }
        
    // Method to create and save the TournamentPlayer relationship
    public String createTournamentPlayer(Long playerId, Long tournamentId) {
        try {
            // Find the tournament and player by their IDs
            Tournament tournament = tournamentService.getTournamentById(tournamentId);
            Player player = playerService.getPlayerById(playerId);

            if (tournament == null || player == null) {
                return "Tournament or Player not found";
            }

            // Create the new TournamentPlayer object
            TournamentPlayer tournamentPlayer = new TournamentPlayer();
            tournamentPlayer.setPlayerId(playerId);
            tournamentPlayer.setTournament(tournament);

            // Save the TournamentPlayer object to the database
            tournamentPlayerRepository.save(tournamentPlayer);

            return "TournamentPlayer relationship created successfully";
        } catch (Exception e) {
            // Handle any errors during saving
            System.err.println("Error creating TournamentPlayer: " + e.getMessage());
            return "Error creating TournamentPlayer";
        }
    }

    // Method to get Player based on id
    public Player getPlayerById(Long firstPlayerId) {
        try {
            // Retrieve TournamentPlayer by playerId
            Optional<TournamentPlayer> tournamentPlayer = tournamentPlayerRepository.findByPlayerId(firstPlayerId);

            // Return the associated playerId from the found TournamentPlayer   
            if (tournamentPlayer.isPresent()) {
                Long playerId = tournamentPlayer.get().getPlayerId(); // Extract the playerId from the TournamentPlayer
                return playerService.getPlayerById(playerId);
            } else {
                System.err.println("Player not found with ID: " + firstPlayerId);
                return null; // Return null or handle error as needed
            }
        } catch (Exception e) {
            System.err.println("Error retrieving PlayerId: " + e.getMessage());
            return null; // Return null or handle error as needed
        }
    }
}
