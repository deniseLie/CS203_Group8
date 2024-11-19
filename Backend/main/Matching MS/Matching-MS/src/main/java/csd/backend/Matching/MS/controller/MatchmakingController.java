package csd.backend.Matching.MS.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import csd.backend.Matching.MS.model.request.MatchmakingRequest;
import csd.backend.Matching.MS.model.request.PlayerIdRequest;
import csd.backend.Matching.MS.service.matchmaking.MatchmakingService;
import csd.backend.Matching.MS.service.player.PlayerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

import java.util.*;

/**
 * MatchmakingController handles the matchmaking operations for players.
 * This includes actions like joining queues, unqueuing players, and handling player statuses.
 */
@RestController
@RequestMapping("/matchmaking")
@CrossOrigin(origins = "${allowed.origins}")
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
     * Endpoint for a player to join the matchmaking queue. This method handles both normal and speed-up join requests.
     * @param joinRequestBody contains player ID, champion ID, and whether it's a speed-up request.
     * @param isSpeedUp flag indicating whether the player is opting for a speed-up queue.
     * @return ResponseEntity with the result of the matchmaking process.
     */
    @PostMapping("/{queueType}/join")
    public ResponseEntity<Map<String, Object>> joinMatchmaking(
        @RequestBody @Valid MatchmakingRequest joinRequestBody, 
        @PathVariable String queueType) {
        
        Long playerId = joinRequestBody.getPlayerId();
        Long championId = joinRequestBody.getChampionId();
        boolean isSpeedUp = "speedup".equalsIgnoreCase(queueType);  // Determine if the queue is speed-up

        logger.info("Player {} joined {} matchmaking with champion {}", playerId, queueType, championId);
        return matchmakingService.joinMatchmaking(playerId, championId, isSpeedUp);
    }

    /**
     * Endpoint for a player to leave the matchmaking queue. Handles both normal and speed-up leave actions.
     * @param request contains player ID.
     * @param queueType type of the queue the player is leaving (e.g., normal, speedup).
     * @return ResponseEntity with the result of the unqueue operation.
     */
    @PostMapping("/{queueType}/leave")
    public ResponseEntity<Map<String, Object>> leaveMatchmaking(
        @RequestBody PlayerIdRequest request, 
        @PathVariable String queueType) {
        
        Long playerId = request.getPlayerId();

        logger.info("Player {} is attempting to leave {} queue.", playerId, queueType);
        return matchmakingService.unqueuePlayerFromQueue(playerId, queueType);
    }
}
