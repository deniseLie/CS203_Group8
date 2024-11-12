package csd.backend.Account.MS.Service.Tournament;

import csd.backend.Account.MS.Exception.*;
import csd.backend.Account.MS.Model.Player.*;
import csd.backend.Account.MS.Model.Tournament.*;
import csd.backend.Account.MS.Repository.Champion.*;
import csd.backend.Account.MS.Repository.Player.*;
import csd.backend.Account.MS.Repository.Tournament.*;
import csd.backend.Account.MS.Service.Champion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TournamentPlayerStatsRepository tournamentPlayerStatsRepository;
    private final PlayerRepository playerRepository;
    private final ChampionRepository championRepository;
    private final ChampionService championService;

    // Constructor injection
    @Autowired
    public TournamentService(
        TournamentRepository tournamentRepository, 
        TournamentPlayerStatsRepository tournamentPlayerStatsRepository, 
        PlayerRepository playerRepository, 
        ChampionRepository championRepository,
        ChampionService championService
    ) { 
        this.tournamentRepository = tournamentRepository;
        this.tournamentPlayerStatsRepository = tournamentPlayerStatsRepository;
        this.playerRepository = playerRepository;
        this.championRepository = championRepository;
        this.championService = championService;
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

    // Create a new tournament and save associated player stats
    public void createAndSaveTournament(Map<String, String> tournamentData) {
        try {
            // Extract values from the map
            int tournamentSize = Integer.parseInt(tournamentData.get("tournamentSize"));
            LocalDateTime timestampStart = LocalDateTime.parse(tournamentData.get("timestampStart"));
            
            Long playerId = Long.parseLong(tournamentData.get("playerId"));
            Long championId = Long.parseLong(tournamentData.get("championId"));
            int killCount = Integer.parseInt(tournamentData.get("killCount"));
            int deathCount = Integer.parseInt(tournamentData.get("deathCount"));
            int finalPlacement = Integer.parseInt(tournamentData.get("finalPlacement"));
            int rankPoints = Integer.parseInt(tournamentData.get("rankPoints"));
            boolean isWin = Boolean.parseBoolean(tournamentData.get("isWin"));
            boolean isAFK = Boolean.parseBoolean(tournamentData.get("isAFK"));
            LocalDateTime endTime = LocalDateTime.parse(tournamentData.get("endTime"));

            // Create Tournament Entity
            Tournament tournament = new Tournament();
            tournament.setTournamentSize(tournamentSize); 
            tournament.setTimestampStart(timestampStart);  
            tournament = tournamentRepository.save(tournament);

            // Create and Save TournamentPlayerStats
            TournamentPlayerStats tournamentPlayerStats = new TournamentPlayerStats();
            tournamentPlayerStats.setTournament(tournament);

            // Retrieve the Player based on playerId
            Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));

            tournamentPlayerStats.setPlayer(player);
            tournamentPlayerStats.setChampionPlayedId(championId);
            tournamentPlayerStats.setRankIdAfterTournament(finalPlacement);  // Setting rank after tournament
            tournamentPlayerStats.setPointObtain(rankPoints);
            tournamentPlayerStats.setFinalPlacement(finalPlacement);
            tournamentPlayerStats.setTimeEndPerPlayer(endTime);  
            tournamentPlayerStats.setKillCount(killCount);  
            tournamentPlayerStats.setDeathCount(deathCount);  
            tournamentPlayerStats.setIsAFK(isAFK); 

            // Save TournamentPlayerStats
            tournamentPlayerStatsRepository.save(tournamentPlayerStats);

            System.out.println("Tournament details processed and saved for player: " + playerId);
        } catch (NumberFormatException e) {
            System.err.println("Error processing tournament data: Invalid number format. " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error processing tournament data: Invalid argument. " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing tournament data: " + e.getMessage());
        }
    }

    // Get Match History
    public List<Map<String, Object>> getPlayerMatchHistory(Long playerId) {
        List<Map<String, Object>> response = new ArrayList<>();

        try {
            // Get all TournamentPlayerStats for the player
            List<TournamentPlayerStats> playerTournamentStats = getTournamentPlayerStatsForPlayer(playerId);

            // Collect all tournamentIds
            List<Long> tournamentIds = playerTournamentStats.stream()
                    .map(tournamentPlayerStats -> tournamentPlayerStats.getTournament().getTournamentId())
                    .distinct()
                    .collect(Collectors.toList());

            // Loop through tournamentIds and gather data
            for (Long tournamentId : tournamentIds) {
                Map<String, Object> tournamentData = new HashMap<>();
                List<TournamentPlayerStats> statsForTournament = getTournamentPlayerStats(tournamentId);

                // Fetch tournament details from the Tournament entity
                Tournament tournament = getTournamentById(tournamentId);
                tournamentData.put("tournamentId", tournamentId);
                tournamentData.put("tournamentSize", tournament.getTournamentSize());
                tournamentData.put("timestampStart", tournament.getTimestampStart().toString());

                List<Map<String, Object>> matchDetails = new ArrayList<>();

                // For each player's stats in the tournament
                for (TournamentPlayerStats stats : statsForTournament) {
                    Map<String, Object> playerData = new HashMap<>();
                    playerData.put("standing", stats.getFinalPlacement());
                    playerData.put("champion", championService.getChampionById(stats.getChampionPlayedId()).getChampionName());
                    playerData.put("playerName", stats.getPlayer().getUsername());
                    playerData.put("kd", stats.getKillCount() + "/" + stats.getDeathCount());
                    playerData.put("kda", (stats.getDeathCount() == 0) ? "Infinity" : String.format("%.2f KDA", (float) stats.getKillCount() / stats.getDeathCount()));
                    playerData.put("lpChange", stats.getPointObtain());
                    playerData.put("time", stats.getTimeEndPerPlayer() != null ? stats.getTimeEndPerPlayer().toLocalTime().toString() : "N/A");
                    playerData.put("date", stats.getTimeEndPerPlayer() != null ? stats.getTimeEndPerPlayer().toLocalDate().toString() : "N/A");
                    playerData.put("isAFK", stats.getIsAFK());
                    matchDetails.add(playerData);
                }

                tournamentData.put("players", matchDetails);
                response.add(tournamentData);
            }
        } catch (Exception e) {
            // Handle any errors and log
            System.err.println("Error processing match history: " + e.getMessage());
        }
        return response;
    }
}
