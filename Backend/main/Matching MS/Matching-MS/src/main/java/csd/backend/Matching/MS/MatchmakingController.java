package csd.backend.Matching.MS;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.*;

@RestController
@RequestMapping("/matchmaking")
public class MatchmakingController {

    private static final Logger logger = LoggerFactory.getLogger(MatchmakingController.class);

    private final MatchmakingService matchmakingService;
    private final PlayerService playerService;

    public MatchmakingController(MatchmakingService matchmakingService, PlayerService playerService) {
        this.matchmakingService = matchmakingService;
        this.playerService = playerService;
    }

    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> joinMatchmaking(@RequestParam String playerId) {
        int maxAttempts = 20;       // Set the maximum number of checks to avoid infinite loops
        int checkInterval = 5000;   // Interval between checks in milliseconds (5 seconds)

        Map<String, Object> response = new HashMap<>();

        try {
            // Check if the player is banned
            Map<String, Object> playerStatus = matchmakingService.checkPlayerStatus(playerId);
            if (playerStatus.containsKey("remainingTime") && (long) playerStatus.get("remainingTime") > 0) {
                long remainingTime = (long) playerStatus.get("remainingTime");
                response.put("message", "You are currently banned. Please try again in " + remainingTime / 1000 + " seconds.");
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); // Return the ResponseEntity here
            }
            
            // Get rank id
            Long rankId = playerService.getPlayerRankId(playerId);
             
            // Queue Player 
            playerService.updatePlayerStatus(playerId, "queue");

            // Keep looping
            for (int attempt = 0; attempt < maxAttempts; attempt++) {

                // Check if enough players are available for the match
                if (matchmakingService.checkForMatch(rankId)) {
                    response.put("message", "Match created successfully.");
                    return new ResponseEntity<>(response, HttpStatus.OK);  // Return the ResponseEntity here
                }

                // Log and wait before the next check
                logger.info("Not enough players to start a match. Retrying in {} ms...", checkInterval);
                try {
                    Thread.sleep(checkInterval);  // Consider making this async if needed
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    response.put("message", "Error during matchmaking process.");
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            // If the max attempts are reached without finding a match, return a timeout message
            logger.info("Max attempts reached without finding a match for player: {}", playerId);
            response.put("message", "Timeout: Unable to find enough players to start a match.");
            return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT); // Return the ResponseEntity here
        } catch (Exception e) {
            logger.error("Error occurred while processing join matchmaking request for player: {}", playerId, e);
            response.put("message", "Error joining matchmaking.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // Return the ResponseEntity here
        }
    }


    @PostMapping("/join/speedupQueue")
    public ResponseEntity<Map<String, Object>> joinSpeedUpMatchmaking(@RequestParam String playerId, @RequestParam String email,
            @RequestParam int rankId) {
                
        Map<String, Object> response = new HashMap<>();
        try {
            // Add player to matchmaking pool
            matchmakingService.addPlayerToPool(playerId, email, "queue", rankId);
            logger.info("Player added to matchmaking pool. Player: {}", playerId);

            // Check if enough players are available for a match
            List<Map<String, AttributeValue>> players = matchmakingService.checkPlayersInSpeedUpQueue(rankId);

            if (players.size() >= 8) {
                // If enough players, create a match and remove them from the queue
                matchmakingService.createMatch(players);
                matchmakingService.removePlayersFromQueue(players);
                logger.info("Match created with players of rank range {} to {}: {}", rankId - 1, rankId + 1, players);
                response.put("message", "Match started with players of rank range " + (rankId - 1) + " to " + (rankId + 1));
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            logger.info("Not enough players to start a match for rank range {} to {}. Current pool size: {}",
                    rankId - 1, rankId + 1, players.size());
            response.put("message", "Waiting for more players of rank range " + (rankId - 1) + " to " + (rankId + 1) + 
                         "... Current pool size: " + players.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while processing join matchmaking request for player: {}, Rank: {}",
                    playerId, rankId, e);
            response.put("message", "Error joining matchmaking.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
