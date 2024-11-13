package csd.backend.Matching.MS;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @CrossOrigin(origins = "http://cs203-bucket.s3-website-ap-southeast-1.amazonaws.com")
    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> joinMatchmaking(@RequestBody JoinRequestBody joinRequestBody) {
        int maxAttempts = 20;       // Set the maximum number of checks to avoid infinite loops
        int checkInterval = 5000;   // Interval between checks in milliseconds (5 seconds)

        Map<String, Object> response = new HashMap<>();

        // Access playerId and championId from joinRequest
        Long playerId = Long.parseLong(joinRequestBody.getPlayerId());
        String championId = joinRequestBody.getChampionId();

        try {
            // Check if the player is banned
            Map<String, Object> playerStatus = matchmakingService.checkPlayerStatus(playerId);
            if (playerStatus.containsKey("remainingTime")) {
                Number remainingTimeValue = (Number) playerStatus.get("remainingTime");
                long remainingTime = remainingTimeValue.longValue();
                if (remainingTime > 0) {
                    response.put("message", "You are currently banned. Please try again in " + remainingTime / 1000 + " seconds.");
                    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
                }
            }
            
            
            // Get rank id
            Long rankId = playerService.getPlayerRankId(playerId);
             
            // Queue Player 
            playerService.updatePlayerStatus(playerId, "queue");
            playerService.updatePlayerChampion(playerId.toString(), championId);

            // Keep looping
            for (int attempt = 0; attempt < maxAttempts; attempt++) {

                // Check if enough players are available for the match
                if (matchmakingService.checkForMatch(rankId, false)) {
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


    @CrossOrigin(origins = "http://cs203-bucket.s3-website-ap-southeast-1.amazonaws.com")
    @PostMapping("/join/speedupQueue")
    public ResponseEntity<Map<String, Object>> joinSpeedUpMatchmaking(@RequestBody JoinRequestBody joinRequestBody) {
        int maxAttempts = 20;       // Set the maximum number of checks to avoid infinite loops
        int checkInterval = 5000;   // Interval between checks in milliseconds (5 seconds)
                
        Map<String, Object> response = new HashMap<>();

        // Access playerId and championId from joinRequest
        Long playerId = Long.parseLong(joinRequestBody.getPlayerId());
        String championId = joinRequestBody.getChampionId();
        try {
            // Check if the player is banned
            Map<String, Object> playerStatus = matchmakingService.checkPlayerStatus(playerId);
            if (playerStatus.containsKey("remainingTime")) {
                Number remainingTimeValue = (Number) playerStatus.get("remainingTime");
                long remainingTime = remainingTimeValue.longValue();
                if (remainingTime > 0) {
                    response.put("message", "You are currently banned. Please try again in " + remainingTime / 1000 + " seconds.");
                    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
                }
            }            
            
            // Get rank id
            Long rankId = playerService.getPlayerRankId(playerId);

            // Queue Player 
            playerService.updatePlayerStatus(playerId, "queue");
            playerService.updatePlayerChampion(playerId.toString(), championId);

            // Keep looping
            for (int attempt = 0; attempt < maxAttempts; attempt++) {

                // Check if enough players are available for the match
                if (matchmakingService.checkForMatch(rankId, true)) {
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

    // Stop queueing (unqueue player)
    @CrossOrigin(origins = "http://cs203-bucket.s3-website-ap-southeast-1.amazonaws.com")
    @PostMapping("/unqueue")
    public ResponseEntity<Map<String, Object>> unqueuePlayer(@RequestParam Long playerId) {
        Map<String, Object> response = new HashMap<>();
        try {

            // Update the player's status in the queue
            playerService.updatePlayerStatus(Long.parseLong(playerId), "not queue");

            // Remove the player's championId
            playerService.removePlayerChampion(playerId);


            response.put("message", "Player successfully removed from queue.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while removing player from the queue: {}", playerId, e);
            response.put("message", "Error removing player from queue.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
