package csd.backend.Matching.MS.service.matchmaking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import csd.backend.Matching.MS.exception.PlayerNotFoundException;
import csd.backend.Matching.MS.model.PlayerStatus;
import csd.backend.Matching.MS.model.QueueType;
import csd.backend.Matching.MS.model.response.TournamentSize;
import csd.backend.Matching.MS.service.player.PlayerService;
import csd.backend.Matching.MS.service.tournament.TournamentService;
import csd.backend.Matching.MS.utils.ResponseUtil;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@Service
public class MatchmakingService {
    
    private final PlayerService playerService;
    private final TournamentService tournamentService;

    private static final Logger logger = LoggerFactory.getLogger(MatchmakingService.class);

    // Maximum number of retry attempts to avoid infinite loops
    private static final int MAX_ATTEMPTS = 20;

    // Interval between each matchmaking check in milliseconds (5 seconds)
    private static final int CHECK_INTERVAL = 5000; // in milliseconds

    // Constructor to inject required services
    public MatchmakingService(TournamentService tournamentService, PlayerService playerService) {
        this.playerService = playerService;
        this.tournamentService = tournamentService;
    }

    // Handle the matchmaking logic for a player joining a queue.
    public ResponseEntity<Map<String, Object>> joinMatchmaking(Long playerId, String championId, boolean isSpeedUp) {

        // Check if the player is banned
        ResponseEntity<Map<String, Object>> banResponse = playerService.checkAndHandlePlayerBan(playerId);
        if (banResponse != null) {
            return banResponse;  // Return the ban response if the player is banned
        }

        try {
            // Get the player's rank ID
            Long rankId = playerService.getPlayerRankId(playerId);

            // Queue Player
            playerService.updatePlayerStatus(playerId, PlayerStatus.QUEUE.getStatus());
            playerService.updatePlayerChampion(playerId, championId);

            // Attempt matchmaking
            return attemptMatchmaking(rankId, isSpeedUp);
        } catch (PlayerNotFoundException e) {
            logger.error("Player {} not found while joining the queue", playerId, e);
            throw new PlayerNotFoundException("Player not found while joining the queue: " + playerId);
        } catch (Exception e) {
            logger.error("Error occurred while processing join matchmaking request for player: {}", playerId, e);
            return ResponseUtil.createInternalServerErrResponse("Error joining matchmaking");
        }
    }    

    // Helper method for retrying matchmaking attempts.
    private ResponseEntity<Map<String, Object>> attemptMatchmaking(Long rankId, boolean speedUpQueue) {
 
        // Start looping
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            // Check if enough players are available for the match
            if (checkForMatch(rankId, speedUpQueue)) {
                return ResponseUtil.createSuccessResponse("Match created successfully.");
            }

            // Log and wait before the next check
            logger.info("Not enough players to start a match. Retrying in {} ms...", CHECK_INTERVAL);
            try {
                Thread.sleep(CHECK_INTERVAL);  // Wait before retrying the matchmaking process
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return ResponseUtil.createInternalServerErrResponse("Error during matchmaking process.");
            }
        }
        // If maxAttempts is reached without finding a match, return a timeout message
        return ResponseUtil.createRequestTimeoutResponse("Timeout: Unable to find enough players to start a match.");
    }

    // Unqueue a player from the matchmaking queue (either normal or speed-up queue)
    private ResponseEntity<Map<String, Object>> unqueuePlayerHelper(Long playerId, String newStatus) {
        try {
            // Update the player's status
            playerService.updatePlayerStatus(playerId, newStatus);

            // Remove the player's champion ID only if the status is being set to "available"
            if (PlayerStatus.AVAILABLE.getStatus().equals(newStatus)) {
                playerService.removePlayerChampion(playerId);
            }

            return ResponseUtil.createSuccessResponse("Player successfully removed from queue");
        } catch (Exception e) {
            logger.error("Error occurred while removing player {} from the queue", playerId, e);
            return ResponseUtil.createInternalServerErrResponse("Error removing player from queue.");
        }
    }

    public ResponseEntity<Map<String, Object>> unqueuePlayerFromQueue(Long playerId, String queueType) {
        if (!QueueType.isValid(queueType)) {
            return ResponseUtil.createBadRequestResponse("Invalid queue type");
        }
        
        if (PlayerStatus.SPEEDUP_QUEUE.getStatus().equals(queueType)) {
            return unqueueFromSpeedUpQueue(playerId);
        }
        return unqueuePlayer(playerId);
    }
    

    // Unqueue a player from the matchmaking queue and mark them as available
    public ResponseEntity<Map<String, Object>> unqueuePlayer(Long playerId) {
        return unqueuePlayerHelper(playerId, PlayerStatus.AVAILABLE.getStatus());
    }

    // Move a player from the speed-up queue to the normal queue.
    public ResponseEntity<Map<String, Object>> unqueueFromSpeedUpQueue(Long playerId) {
        return unqueuePlayerHelper(playerId, PlayerStatus.QUEUE.getStatus());
    }


    // Checks if enough players are available in the queue to create a match.
    public boolean checkForMatch(Long rankId, boolean isSpeedUp) {
        int maxPlayers = TournamentSize.getTournamentSize();  // Maximum players needed for a match

        // Get players in the queue with the same rankId
        List<Map<String, AttributeValue>> players = isSpeedUp 
                ? playerService.checkPlayersInSpeedUpQueue(rankId) 
                : playerService.checkPlayersInQueue(rankId);

        // Check if there are enough players in the queue
        if (players.size() >= maxPlayers) {
            // Create a match with the first MAX_PLAYERS players
            List<Map<String, AttributeValue>> playersToMatch = players.subList(0, maxPlayers);
            tournamentService.createTournament(playersToMatch);
            return true;
        }
        return false;
    }
}
