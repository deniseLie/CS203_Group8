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
     * Endpoint for a player to join the matchmaking queue.
     * @param joinRequestBody contains player ID and champion ID
     * @return ResponseEntity with the result of the matchmaking process
     */
    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> joinMatchmaking(@RequestBody @Valid MatchmakingRequest joinRequestBody) {
        Long playerId = joinRequestBody.getPlayerId();
        Long championId = joinRequestBody.getChampionId();
        boolean isSpeedUp = joinRequestBody.getIsSpeedUp();

        logger.info("Player {} joined matchmaking with champion {}", playerId, championId);
        return matchmakingService.joinMatchmaking(playerId, championId, isSpeedUp);
    }

    @PostMapping("/unqueue/{queueType}")
    public ResponseEntity<Map<String, Object>> unqueuePlayer(@RequestBody PlayerIdRequest request, @PathVariable String queueType) {
        return matchmakingService.unqueuePlayerFromQueue(request.getPlayerId(), queueType);
    }
}
