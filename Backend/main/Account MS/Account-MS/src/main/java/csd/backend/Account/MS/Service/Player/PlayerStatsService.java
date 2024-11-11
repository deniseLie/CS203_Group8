package csd.backend.Account.MS.Service.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csd.backend.Account.MS.Model.Player.*;
import csd.backend.Account.MS.Repository.Player.*;
import csd.backend.Account.MS.Repository.Champion.*;

@Service
public class PlayerStatsService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerOverallStatsRepository playerOverallStatsRepository;
    
    @Autowired
    private PlayerChampionStatsRepository playerChampionStatsRepository;

    // Update overall stats (after a match)
    public void updateOverallStats(Long playerId, int rankPoints, double kdRate, int finalPlacement, boolean isWin) {
        // Retrieve the player's overall stats
        PlayerOverallStats stats = playerOverallStatsRepository.findByPlayerId(playerId);
        
        // Create new stats if they don't exist
        if (stats == null) {
            stats = new PlayerOverallStats();
            stats.setId(playerId);
            stats.setRankId(1); // Default rank if none exists
            stats.setRankPoints(rankPoints);
            stats.setTotalWins(isWin ? 1 : 0);  
            stats.setTotalNumberOfMatches(1);  
            stats.setOverallAveragePlace((double)finalPlacement);  
            stats.setOverallKdRate(kdRate);  

        // Update existing stats
        } else {

            // Update #
            if (isWin) stats.setTotalWins(stats.getTotalWins() + 1);
            stats.setTotalFirstPlaceMatches(stats.getTotalFirstPlaceMatches() + 1);
            stats.setTotalNumberOfMatches(stats.getTotalNumberOfMatches() + 1);

            // Update stats (rankId + rank points + KD rate + average place + first place percentage)
            stats.setRankId(1);
            stats.setRankPoints(stats.getRankPoints() + rankPoints);  
            stats.setOverallKdRate(calculateWeightedAverage(stats.getOverallKdRate(), stats.getTotalNumberOfMatches(), kdRate)); 
            stats.setOverallAveragePlace(calculateWeightedAverage(stats.getOverallAveragePlace(), stats.getTotalNumberOfMatches(), finalPlacement)); 
        }

        // Save the updated stats
        playerOverallStatsRepository.save(stats);
    }

    // Update champion stats (after a match)
    public void updateChampionStats(Long playerId, Long championId, double kdRate, double finalPlacement, boolean isWin) {
        PlayerChampionStats championStats = playerChampionStatsRepository.findByPlayerIdAndChampionId(playerId, championId);
        
        // Create new stats if they don't exist
        if (championStats == null) {
            championStats = new PlayerChampionStats();
            championStats.setPlayerId(playerId);
            championStats.setAveragePlace(finalPlacement);
            championStats.setChampionId(championId);
            championStats.setKdRate(kdRate);
            championStats.setTotalWins(isWin ? 1 : 0); 
            championStats.setTotalMatchNumber(1); 

        // Update existing stats
        } else {

            // Update #
            if (isWin) championStats.setTotalWins(championStats.getTotalWins() + 1);
            championStats.setTotalMatchNumber(championStats.getTotalMatchNumber() + 1);

            // Update the KD rate and average place based on the previous stats
            championStats.setKdRate(calculateWeightedAverage(championStats.getKdRate(), championStats.getTotalMatchNumber(), kdRate)); 
            championStats.setAveragePlace(calculateWeightedAverage(championStats.getAveragePlace(), championStats.getTotalMatchNumber(), finalPlacement)); 
        }

        
        // Save the updated champion stats
        playerChampionStatsRepository.save(championStats);
    }

    // Calculate the updated percentage
    private double calculateWeightedAverage(double currentValue, int totalMatchCount, double newValue) {
        return (currentValue * (totalMatchCount - 1) + newValue) / totalMatchCount;
    }
}
