package csd.backend.Matching.MS;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/matchmaking")
public class MatchmakingController {

    private static final Logger logger = LoggerFactory.getLogger(MatchmakingController.class);
    
    @Autowired
    private MatchmakingService matchmakingService;

    @PostMapping("/join")
    public String joinMatchmaking(@RequestParam String playerName, @RequestParam String rank) {
        logger.info("Received request to join matchmaking. Player: {}, Email: {}", playerName, rank);
        int maxAttempts = 20;       // Set the maximum number of checks to avoid infinite loops
        int checkInterval = 5000;   // Interval between checks in milliseconds (5 seconds)
        int rankId = Integer.parseInt(rank);  // Parse rankId to integer if needed

        try {
            // Queue Player 
            matchmakingService.updatePlayerStatus(playerName, "queue");
            logger.info("Player status updated to 'queue'. Player: {}", playerName);

            for (int attempt = 0; attempt < maxAttempts; attempt++) {

                if (matchmakingService.checkForMatch(rankId)) {
                    logger.info("Match successfully created for rank: {}", rankId);
                    return "Match created successfully.";
                }

                // Log and wait before the next check
                logger.info("Not enough players to start a match. Retrying in {} ms...", checkInterval);
                Thread.sleep(checkInterval);
            }

            // If the max attempts are reached without finding a match, return a timeout message
            logger.info("Max attempts reached without finding a match for player: {}", playerName);
            return "Timeout: Unable to find enough players to start a match.";
        } catch (Exception e) {
            logger.error("Error occurred while processing join matchmaking request for player: {}", playerName, e);
            return "Error joining matchmaking.";
        }
    }

}
