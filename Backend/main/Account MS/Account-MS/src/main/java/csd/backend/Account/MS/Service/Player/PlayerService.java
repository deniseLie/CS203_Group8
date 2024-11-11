package csd.backend.Account.MS.Service.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csd.backend.Account.MS.Exception.*;
import csd.backend.Account.MS.Model.Player.*;
import csd.backend.Account.MS.Repository.Player.*;
import csd.backend.Account.MS.Service.Player.*; 
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    @Autowired 
    private PlayerRepository playerRepository;
    
    @Autowired
    private PlayerOverallStatsRepository playerOverallStatsRepository;

    @Autowired
    private PlayerChampionStatsRepository playerChampionStatsRepository;

    @Autowired
    private PlayerStatsService playerStatsService; 

    // Insert new user into database
    public void registerUser(Player player) {
        // Check if player already exists
        if (playerRepository.findByUsername(player.getUsername()).isPresent()) {
            throw new PlayerRegisterExisted(player.getUsername());
        }
        playerRepository.save(player);
    }

    // Handle match completion and recalculate stats
    @Transactional
    public void handleMatchCompletion(Long playerId, int championId, double kdRate, int finalPlacement, int rankPoints, boolean isWin) {
        // Check if player already exists
        if (!playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException(playerId);
        }

        // Update Player stats in overall stats and champion stats
        playerStatsService.updateOverallStats(playerId, rankPoints, kdRate, finalPlacement, isWin);
        playerStatsService.updateChampionStats(playerId, championId, kdRate, finalPlacement, isWin);
    }

    // Get the top 3 played champions for the player
    public List<PlayerChampionStats> getTop3PlayedChampions(Long playerId) {
        // Check if the player exists
        if (!playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException(playerId);
        }

        List<PlayerChampionStats> championStats = playerChampionStatsRepository.findByPlayerId(playerId);

        // Sort by total matches played, descending, limit to top 3
        if (championStats != null && !championStats.isEmpty()) {
            return championStats.stream()
                    .sorted(Comparator.comparingInt(PlayerChampionStats::getTotalMatchNumber).reversed())
                    .limit(3)
                    .collect(Collectors.toList());
        
        // Handle case where no data found
        } else {
            throw new PlayerChampionStatsNotFoundException(playerId);
        }
    }

    // Get player statistics (total matches, average place, first place percentage)
    public Map<String, Object> getPlayerStats(Long playerId) {
        // Check if the player exists
        if (!playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException(playerId);
        }

        Optional<PlayerOverallStats> playerStatsOpt = Optional.ofNullable(playerOverallStatsRepository.findByPlayerId(playerId));
        Map<String, Object> statsMap = new HashMap<>();

        if (playerStatsOpt.isPresent()) {
            PlayerOverallStats stats = playerStatsOpt.get();
            double firstPlacePercentage = (double) stats.getTotalFirstPlaceMatches() / stats.getTotalNumberOfMatches() * 100;

            // Return player stats as a map
            statsMap.put("totalMatches", stats.getTotalNumberOfMatches());
            statsMap.put("averagePlace", stats.getOverallAveragePlace());
            statsMap.put("firstPlacePercentage", firstPlacePercentage);

        } else {
            statsMap.put("message", "Player stats not found");
        }
    
        return statsMap;
    }
}
