package csd.backend.Account.MS.service.tournament;

import csd.backend.Account.MS.exception.player.PlayerNotFoundException;
import csd.backend.Account.MS.model.player.*;
import csd.backend.Account.MS.model.tournament.*;
import csd.backend.Account.MS.repository.player.*;
import csd.backend.Account.MS.repository.tournament.*;
import csd.backend.Account.MS.service.champion.*;
import csd.backend.Account.MS.exception.tournament.*;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TournamentPlayerStatsRepository tournamentPlayerStatsRepository;
    private final PlayerRepository playerRepository;
    private final ChampionService championService;

    // Constructor injection for dependencies
    public TournamentService(
        TournamentRepository tournamentRepository, 
        TournamentPlayerStatsRepository tournamentPlayerStatsRepository, 
        PlayerRepository playerRepository, 
        ChampionService championService
    ) { 
        this.tournamentRepository = tournamentRepository;
        this.tournamentPlayerStatsRepository = tournamentPlayerStatsRepository;
        this.playerRepository = playerRepository;
        this.championService = championService;
    }

    // Get all tournaments from the repository
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    // Get a tournament by its ID, throwing an exception if not found
    public Tournament getTournamentById(Long tournamentId) {
        return tournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new TournamentNotFoundException("Tournament not found with ID: " + tournamentId));
    }

    // Get all player stats for a specific tournament
    public List<TournamentPlayerStats> getTournamentPlayerStats(Long tournamentId) {
        return tournamentPlayerStatsRepository.findByTournamentTournamentId(tournamentId);
    }

    // Get player stats for a particular player across all tournaments
    public List<TournamentPlayerStats> getTournamentPlayerStatsForPlayer(Long playerId) {
        return tournamentPlayerStatsRepository.findByPlayerId(playerId);
    }

    // Create a new tournament and save associated player stats
    public Tournament createAndSaveTournament(Map<String, String> tournamentData) {
        try {
            // Create tournament entity from data
            Tournament tournament = createTournamentEntity(tournamentData);
            
            // Save player stats associated with the tournament
            saveTournamentPlayerStats(tournament, tournamentData);
            return tournament;
        } catch (NumberFormatException e) {
            System.err.println("Error processing tournament data: Invalid number format. " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error processing tournament data: Invalid argument. " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing tournament data: " + e.getMessage());
        }
        return null; // Return null if any error occurs
    }

    // Helper method to create and save tournament entity
    private Tournament createTournamentEntity(Map<String, String> tournamentData) {
        Tournament tournament = new Tournament();
        tournament.setTournamentSize(Integer.parseInt(tournamentData.get("tournamentSize"))); // Tournament size
        tournament.setTimestampStart(LocalDateTime.parse(tournamentData.get("timestampStart"))); // Start time
        return tournamentRepository.save(tournament); // Save tournament entity and return it
    }

    // Helper method to save the tournament player stats after a tournament
    private void saveTournamentPlayerStats(Tournament tournament, Map<String, String> tournamentData) {
        TournamentPlayerStats tournamentPlayerStats = new TournamentPlayerStats();
        Player player = playerRepository.findById(Long.parseLong(tournamentData.get("playerId")))
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        // Populate the stats for the player in this tournament
        tournamentPlayerStats.setPlayer(player);
        tournamentPlayerStats.setTournament(tournament);
        tournamentPlayerStats.setChampionPlayedId(Long.parseLong(tournamentData.get("championId")));
        tournamentPlayerStats.setRankIdAfterTournament(Integer.parseInt(tournamentData.get("finalPlacement")));
        tournamentPlayerStats.setPointObtain(Integer.parseInt(tournamentData.get("rankPoints")));
        tournamentPlayerStats.setFinalPlacement(Integer.parseInt(tournamentData.get("finalPlacement")));
        tournamentPlayerStats.setTimeEndPerPlayer(LocalDateTime.parse(tournamentData.get("endTime")));
        tournamentPlayerStats.setKillCount(Integer.parseInt(tournamentData.get("killCount")));
        tournamentPlayerStats.setDeathCount(Integer.parseInt(tournamentData.get("deathCount")));
        tournamentPlayerStats.setIsAFK(Boolean.parseBoolean(tournamentData.get("isAFK")));

        // Save player stats for the tournament
        tournamentPlayerStatsRepository.save(tournamentPlayerStats);
    }

    // Get player match history based on the player's stats
    public List<Map<String, Object>> getPlayerMatchHistory(Long playerId) {
        List<Map<String, Object>> response = new ArrayList<>();

        // Get all tournament stats for the player
        List<TournamentPlayerStats> playerTournamentStats = getTournamentPlayerStatsForPlayer(playerId);
        
        // Collect distinct tournament IDs
        List<Long> tournamentIds = playerTournamentStats.stream()
                .map(tournamentPlayerStats -> tournamentPlayerStats.getTournament().getTournamentId())
                .distinct()
                .collect(Collectors.toList());

        // For each tournament, get its details and associated player stats
        for (Long tournamentId : tournamentIds) {
            Map<String, Object> tournamentData = new HashMap<>();
            populateTournamentData(tournamentId, tournamentData);  // Populate data for each tournament
            response.add(tournamentData);
        }
        return response; // Return the populated response
    }

    // Helper method to populate tournament data
    private void populateTournamentData(Long tournamentId, Map<String, Object> tournamentData) {
        Tournament tournament = getTournamentById(tournamentId); // Fetch tournament details
        tournamentData.put("tournamentId", tournamentId);
        tournamentData.put("tournamentSize", tournament.getTournamentSize());
        tournamentData.put("timestampStart", tournament.getTimestampStart().toString());

        // Collect match details for players in the tournament
        List<Map<String, Object>> matchDetails = new ArrayList<>();
        List<TournamentPlayerStats> statsForTournament = getTournamentPlayerStats(tournamentId);

        // For each player's stats in the tournament, collect relevant details
        for (TournamentPlayerStats stats : statsForTournament) {
            matchDetails.add(collectPlayerStats(stats));
        }

        tournamentData.put("players", matchDetails); // Add player details to tournament data
    }

    // Helper method to collect player stats for the match history
    private Map<String, Object> collectPlayerStats(TournamentPlayerStats stats) {
        Map<String, Object> playerData = new HashMap<>();
        playerData.put("standing", stats.getFinalPlacement()); // Player's standing in tournament
        playerData.put("champion", championService.getChampionById(stats.getChampionPlayedId())); // Played champion
        playerData.put("playerName", stats.getPlayer().getUsername()); // Player's name
        playerData.put("kd", stats.getKillCount() + "/" + stats.getDeathCount()); // Kill/Death ratio
        playerData.put("kda", stats.getDeathCount() == 0 ? "Infinity" : String.format("%.2f KDA", (float) stats.getKillCount() / stats.getDeathCount())); // KDA ratio
        playerData.put("lpChange", stats.getPointObtain()); // LP change after tournament
        playerData.put("time", stats.getTimeEndPerPlayer() != null ? stats.getTimeEndPerPlayer().toLocalTime().toString() : "N/A"); // Match end time
        playerData.put("date", stats.getTimeEndPerPlayer() != null ? stats.getTimeEndPerPlayer().toLocalDate().toString() : "N/A"); // Match date
        playerData.put("isAFK", stats.getIsAFK()); // If the player was AFK during the match
        return playerData; // Return the collected player stats
    }
}
