package csd.backend.Admin.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csd.backend.Admin.Model.*;
import csd.backend.Admin.Repository.*;

@Service
public class TournamentPlayerService {

    private final TournamentPlayerRepository tournamentPlayerRepository;
    
    @Autowired
    public TournamentPlayerService(TournamentPlayerRepository tournamentPlayerRepository) {
        this.tournamentPlayerRepository = tournamentPlayerRepository;
    }
        
    // Method to create and save the TournamentPlayer relationship
    public String createTournamentPlayer(TournamentPlayer tournamentPlayer) {
        try {
            // Save the TournamentPlayer object to the database
            tournamentPlayerRepository.save(tournamentPlayer);

            // Return success message
            return "TournamentPlayer relationship created successfully";
        } catch (Exception e) {
            // Handle any errors during saving
            System.err.println("Error creating TournamentPlayer: " + e.getMessage());
            return "Error creating TournamentPlayer";
        }
    }
}
