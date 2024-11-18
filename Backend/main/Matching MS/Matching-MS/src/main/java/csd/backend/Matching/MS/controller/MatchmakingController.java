package csd.backend.Matching.MS.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import csd.backend.Matching.MS.model.request.MatchmakingRequest;
import csd.backend.Matching.MS.service.matchmaking.MatchmakingService;
import csd.backend.Matching.MS.service.player.PlayerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

/**
 * MatchmakingController handles requests related to matchmaking, including joining queues
 * and managing players' status within queues.
 */
@RestController
@RequestMapping("/matchmaking")
public class MatchmakingController {

    private static final Logger logger = LoggerFactory.getLogger(MatchmakingController.class);

    private final MatchmakingService matchmakingService;
    private final PlayerService playerService;

    // Constructor to inject the required services
    public MatchmakingController(MatchmakingService matchmakingService, PlayerService playerService) {
        this.matchmakingService = matchmakingService;
        this.playerService = playerService;
    }

    /**
     * Helper method to check if the player is banned and return an appropriate response if needed.
     * @param playerId the ID of the player to check
     * @return ResponseEntity with the error message if the player is banned, or null if the player is not banned
     */
    private ResponseEntity<Map<String, Object>> checkAndHandlePlayerBan(Long playerId) {
        Map<String, Object> playerStatus = matchmakingService.checkPlayerStatus(playerId);
        if (playerStatus.containsKey("remainingTime")) {
            Number remainingTimeValue = (Number) playerStatus.get("remainingTime");
            long remainingTime = remainingTimeValue.longValue();
            if (remainingTime > 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "You are currently banned. Please try again in " + remainingTime / 1000 + " seconds.");
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); // Return forbidden response
            }
        }
        return null;  // Return null if the player is not banned
    }

    /**
     * Helper method for retrying matchmaking attempts.
     * @param rankId the rank ID of the player
     * @param speedUpQueue flag indicating if the player is in the speed-up queue
     * @return ResponseEntity with success or timeout message
     */
    private ResponseEntity<Map<String, Object>> attemptMatchmaking(Long rankId, boolean speedUpQueue) {
        int maxAttempts = 20;       // Maximum number of retry attempts to avoid infinite loops
        int checkInterval = 5000;   // Interval between each matchmaking check in milliseconds (5 seconds)

        Map<String, Object> response = new HashMap<>();

        // Start looping
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            // Check if enough players are available for the match
            if (matchmakingService.checkForMatch(rankId, speedUpQueue)) {
                response.put("message", "Match created successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);  // Return successful response
            }

            // Log and wait before the next check
            logger.info("Not enough players to start a match. Retrying in {} ms...", checkInterval);
            try {
                Thread.sleep(checkInterval);  // Wait before retrying the matchmaking process
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                response.put("message", "Error during matchmaking process.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        // If maxAttempts is reached without finding a match, return a timeout message
        response.put("message", "Timeout: Unable to find enough players to start a match.");
        return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT); // Return request timeout response
    }

    /**
     * Endpoint for a player to join the matchmaking queue.
     * @param joinRequestBody contains player ID and champion ID
     * @return ResponseEntity with the result of the matchmaking process
     */
    @CrossOrigin(origins = "http://cs203-bucket.s3-website-ap-southeast-1.amazonaws.com")
    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> joinMatchmaking(@RequestBody MatchmakingRequest joinRequestBody) {
        Long playerId = Long.parseLong(joinRequestBody.getPlayerId());
        String championId = joinRequestBody.getChampionId();

        // Check if the player is banned
        ResponseEntity<Map<String, Object>> banResponse = checkAndHandlePlayerBan(playerId);
        if (banResponse != null) {
            return banResponse;  // Return the ban response if the player is banned
        }

        try {
            // Get the player's rank ID
            Long rankId = playerService.getPlayerRankId(playerId);

            // Queue Player 
            playerService.updatePlayerStatus(playerId, "queue");
            playerService.updatePlayerChampion(playerId, championId);

            // Attempt matchmaking
            return attemptMatchmaking(rankId, false);  // Normal queue attempt
        } catch (Exception e) {
            logger.error("Error occurred while processing join matchmaking request for player: {}", playerId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error joining matchmaking.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for a player to join the speed-up matchmaking queue.
     * @param joinRequestBody contains player ID and champion ID
     * @return ResponseEntity with the result of the matchmaking process
     */
    @CrossOrigin(origins = "http://cs203-bucket.s3-website-ap-southeast-1.amazonaws.com")
    @PostMapping("/join/speedupQueue")
    public ResponseEntity<Map<String, Object>> joinSpeedUpMatchmaking(@RequestBody MatchmakingRequest joinRequestBody) {
        Long playerId = Long.parseLong(joinRequestBody.getPlayerId());
        String championId = joinRequestBody.getChampionId();

        // Check if the player is banned
        ResponseEntity<Map<String, Object>> banResponse = checkAndHandlePlayerBan(playerId);
        if (banResponse != null) {
            return banResponse;  // Return the ban response if the player is banned
        }

        try {
            // Get the player's rank ID
            Long rankId = playerService.getPlayerRankId(playerId);

            // Queue Player 
            playerService.updatePlayerStatus(playerId, "queue");
            playerService.updatePlayerChampion(playerId, championId);

            // Attempt matchmaking in the speed-up queue
            return attemptMatchmaking(rankId, true);  // Speed-up queue attempt
        } catch (Exception e) {
            logger.error("Error occurred while processing join speed-up matchmaking request for player: {}", playerId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error joining speed-up matchmaking.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to unqueue (remove) a player from the matchmaking queue.
     * @param playerId the ID of the player to remove from the queue
     * @return ResponseEntity with the result of the unqueue process
     */
    @CrossOrigin(origins = "http://cs203-bucket.s3-website-ap-southeast-1.amazonaws.com")
    @PostMapping("/unqueue")
    public ResponseEntity<Map<String, Object>> unqueuePlayer(@RequestParam Long playerId) {
        try {
            // Update the player's status to "not queue"
            playerService.updatePlayerStatus(playerId, "not queue");

            // Remove the player's champion ID
            playerService.removePlayerChampion(playerId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Player successfully removed from queue.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while removing player from the queue: {}", playerId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error removing player from queue.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to unqueue (remove) a player from the speed-up queue and move them back to the normal queue.
     * @param playerId the ID of the player to remove from the speed-up queue
     * @return ResponseEntity with the result of the unqueue process
     */
    @CrossOrigin(origins = "http://cs203-bucket.s3-website-ap-southeast-1.amazonaws.com")
    @PostMapping("/unqueue/speedUpQueue")
    public ResponseEntity<Map<String, Object>> unqueueFromSpeedUpQueue(@RequestParam Long playerId) {
        try {
            // Update the player's status to "queue" to move them back to the normal queue
            playerService.updatePlayerStatus(playerId, "queue");

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Player successfully moved back to normal queue from speed-up queue.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while removing player from the speed-up queue: {}", playerId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error removing player from speed-up queue.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
