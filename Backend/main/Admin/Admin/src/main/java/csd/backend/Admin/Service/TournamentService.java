package csd.backend.Admin.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import csd.backend.Admin.Repository.*;
import csd.backend.Admin.Repository.User.*;

import java.util.*;

import csd.backend.Admin.Model.DTO.*;
import csd.backend.Admin.Model.Tournament.*;
import csd.backend.Admin.Model.User.Player;

@Service
public class TournamentService {
    
    private final TournamentRepository tournamentRepository; // Tournament ADMIN FUNCT
    private final TournamentPlayerRepository tournamentPlayerRepository;
    private final PlayerRepository playerRepository;
    private final TournamentRoundRepository tournamentRoundRepository;
    
    @Autowired
    public TournamentService(TournamentRepository tournamentRepository, 
                             TournamentPlayerRepository tournamentPlayerRepository,
                             PlayerRepository playerRepository, // Injected the PlayerRepository
                             TournamentRoundRepository tournamentRoundRepository) { // Injected the TournamentRoundRepository
        this.tournamentRepository = tournamentRepository;
        this.tournamentPlayerRepository = tournamentPlayerRepository;
        this.playerRepository = playerRepository;
        this.tournamentRoundRepository = tournamentRoundRepository;
    }

    // Function create tournament
    public Tournament createTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    // Method get all tournament
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    // Find tournament by ID
    public Tournament getTournamentById(Long tournamentId) {
        return tournamentRepository.findById(tournamentId).orElse(null); // Return null if not found
    }

    // Get all tournament details
    public List<TournamentDTO> getAllTournamentsDTO() {
        List<Tournament> tournaments = getAllTournaments();  // Get all tournaments
        List<TournamentDTO> tournamentDTOs = new ArrayList<>();
    
        // For each tournament, get its details and add to the list
        for (Tournament tournament : tournaments) {
            TournamentDTO tournamentDTO = getTournamentDetails(tournament.getTournamentId());
            if (tournamentDTO != null) {
                tournamentDTOs.add(tournamentDTO);
            }
        }
    
        return tournamentDTOs;
    }
    
    // Method to get tournament details with additional calculations
    public TournamentDTO getTournamentDetails(Long tournamentId) {
        Tournament tournament = getTournamentById(tournamentId);
        if (tournament == null) return null;

        // Retrieve players associated with the tournament
        List<TournamentPlayer> tournamentPlayers = tournamentPlayerRepository.findByTournament(tournament);
        List<PlayerDTO> players = new ArrayList<>();

        // Retrieve player details
        for (TournamentPlayer tournamentPlayer : tournamentPlayers) {
            Long playerId = tournamentPlayer.getPlayerId();
            Optional<Player> playerOpt = playerRepository.findById(playerId);
            if (playerOpt.isPresent()) {
                Player player = playerOpt.get();
                PlayerDTO playerDTO = new PlayerDTO();
                playerDTO.setPlayerId(player.getPlayerId());
                playerDTO.setPlayerName(player.getPlayerName());
                players.add(playerDTO);
            }
        }

        // Calculate totalRounds (log2 of tournamentSize)
        int totalRounds = (int) (Math.log(tournament.getTournamentSize()) / Math.log(2));

        // Find current round by checking the highest roundNumber from TournamentRound
        int currentRound = getCurrentRound(tournament);

        // Build and return the TournamentDTO
        TournamentDTO tournamentDTO = new TournamentDTO();
        tournamentDTO.setTournamentId(tournament.getTournamentId());
        tournamentDTO.setTournamentSize(tournament.getTournamentSize());
        tournamentDTO.setStatus(tournament.getTimestampEnd() != null ? "completed" : "ongoing");
        tournamentDTO.setTimestampStart(tournament.getTimestampStart());
        tournamentDTO.setTimeStampEnd(tournament.getTimestampEnd());
        tournamentDTO.setPlayers(players);
        tournamentDTO.setTotalRounds(totalRounds);
        tournamentDTO.setCurrentRound(currentRound);

        return tournamentDTO;
    }

    // Get tournament constant size
    public int getTournamentSize() {
        return new TournamentSize().getTournamentSize();
    }
    
    // Method to update tournament constant size
    public int updateTournamentSize(int newTournamentSize) {
        // Retrieve all tournaments
        
    }

    // Get Latest Round of a tournament
    private int getCurrentRound(Tournament tournament) {
        // Find the latest round from TournamentRound table based on tournament ID
        TournamentRound latestRound = tournamentRoundRepository
            .findTopByTournamentOrderByRoundNumberDesc(tournament)
            .orElse(null);  // If no rounds, return null
    
        // If there is a round, return the round number; else, return 0 (no rounds)
        return latestRound != null ? latestRound.getRoundNumber() : 0;
    }

    // not necessary.. i think..
    // public String deleteMatch(int matchId) {
    //     if (tournamentRepository.existsById(matchId)) {
    //         tournamentRepository.deleteById(matchId);
    //         return "Tournament deleted successfully";
    //     } else {
    //         return "Tournament not found";
    //     }
    // }
}
