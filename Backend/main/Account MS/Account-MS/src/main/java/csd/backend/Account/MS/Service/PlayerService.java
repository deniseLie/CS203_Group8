package csd.backend.Account.MS.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csd.backend.Account.MS.Exception.PlayerChampionStatsNotFoundException;
import csd.backend.Account.MS.Exception.PlayerNotFoundException;
import csd.backend.Account.MS.Exception.PlayerRegisterExisted;
import csd.backend.Account.MS.Model.*;
import csd.backend.Account.MS.Repository.*;
import csd.backend.Account.MS.Service.*; 
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
    private StatsService statsService; 

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
        statsService.updateOverallStats(playerId, rankPoints, kdRate, finalPlacement, isWin);
        statsService.updateChampionStats(playerId, championId, kdRate, finalPlacement, isWin);
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
