package csd.backend.Account.MS.service.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import csd.backend.Account.MS.exception.PlayerNotFoundException;
import csd.backend.Account.MS.model.player.*;
import csd.backend.Account.MS.repository.player.*;
import csd.backend.Account.MS.service.SqsService;
import csd.backend.Account.MS.service.rank.RankService;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import java.util.*;

@Service
public class PlayerStatsService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerOverallStatsRepository playerOverallStatsRepository;
    
    @Autowired
    private PlayerChampionStatsRepository playerChampionStatsRepository;

    @Autowired
    private RankService rankService;

    @Autowired
    private SqsService sqsService;

    // Get player's rank id 
    public String getPlayerRankName(Long playerId) {
        // Check if player already exists
        if (!playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException(playerId);
        }

        // Retrieve player's rank id
        Long rankId = playerOverallStatsRepository.findRankIdByPlayerId(playerId);

        // Retrieve rank name
        String rankName = rankService.getRankNameById(rankId);

        // Prepare the message body
        return rankName;

    }

    // Handle match completion and recalculate stats
    public void handleMatchCompletion(Long playerId, Long championId, double kdRate, int finalPlacement, int rankPoints, boolean isWin) {
        // Check if player already exists
        if (!playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException(playerId);
        }

        // Update Player stats in overall stats and champion stats
        updateOverallStats(playerId, rankPoints, kdRate, finalPlacement, isWin);
        updateChampionStats(playerId, championId, kdRate, finalPlacement, isWin);
    }

    // Update overall stats (after a match)
    public void updateOverallStats(Long playerId, int rankPoints, double kdRate, int finalPlacement, boolean isWin) {
        // Retrieve the player's overall stats
        PlayerOverallStats stats = playerOverallStatsRepository.findByPlayerId(playerId);

        // Update rank points + rank ID 
        int newRankPoints = stats != null ? stats.getRankPoints() + rankPoints : rankPoints;
        Long updatedRankId = rankService.getRankIdByPoints(newRankPoints);
        
        // Create new stats if they don't exist
        if (stats == null) {
            stats = new PlayerOverallStats();
            stats.setId(playerId);
            stats.setRankId(updatedRankId); 
            stats.setRankPoints(newRankPoints);
            stats.setTotalWins(isWin ? 1 : 0);  
            stats.setTotalNumberOfMatches(1);  
            stats.setOverallAveragePlace((double)finalPlacement);  
            stats.setOverallKdRate(kdRate);  

        // Update existing stats
        } else {

            // Update total wins and match stats
            if (isWin) stats.setTotalWins(stats.getTotalWins() + 1);
            stats.setTotalFirstPlaceMatches(stats.getTotalFirstPlaceMatches() + 1);
            stats.setTotalNumberOfMatches(stats.getTotalNumberOfMatches() + 1);

            // Update stats (rankId + rank points + KD rate + average place + first place percentage)
            stats.setRankId(updatedRankId);
            stats.setRankPoints(newRankPoints);  
            stats.setOverallKdRate(calculateWeightedAverage(stats.getOverallKdRate(), stats.getTotalNumberOfMatches(), kdRate)); 
            stats.setOverallAveragePlace(calculateWeightedAverage(stats.getOverallAveragePlace(), stats.getTotalNumberOfMatches(), finalPlacement)); 
        }

        // Save the updated stats
        playerOverallStatsRepository.save(stats);
        
        // Prepare the message body as JSON
        updateNewRankIdToMachmakingMS(playerId, updatedRankId);
    }

    // Send New Rank Id to matchmaking microservice 
    private void updateNewRankIdToMachmakingMS (Long playerId, Long updatedRankId) {
        ObjectMapper objectMapper = new ObjectMapper();
        String messageBody = "";
        try {
            ObjectNode messageJson = objectMapper.createObjectNode();
            messageJson.put("playerId", playerId);
            messageJson.put("rankId", updatedRankId);
            messageBody = objectMapper.writeValueAsString(messageJson);
        } catch (Exception e) {
            System.err.println("Failed to create message body JSON: " + e.getMessage());
        }

        // Prepare message attributes (optional)
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("actionType", MessageAttributeValue.builder()
                .stringValue("updatePlayerProfile")
                .dataType("String")
                .build());

        // Send New Rank To Matchmaking
        sqsService.sendMessageToQueue("matchmaking", messageBody, messageAttributes);
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
