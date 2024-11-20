package csd.backend.Account.MS.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import csd.backend.Account.MS.DTO.PlayerProfileUpdateRequest;
import csd.backend.Account.MS.exception.player.PlayerNotFoundException;
import csd.backend.Account.MS.model.player.*;
import csd.backend.Account.MS.service.player.*;
import csd.backend.Account.MS.service.tournament.*;
import csd.backend.Account.MS.utils.*;

import java.util.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
  
     
    private final PlayerService playerService;
    private final PlayerStatsService playerStatsService;
    private final TournamentService tournamentService;

    public AccountController(
        PlayerService playerService, PlayerStatsService playerStatsService, TournamentService tournamentService
    ) {
        this.playerService = playerService;
        this.playerStatsService = playerStatsService;
        this.tournamentService = tournamentService;
    }

    // Edit Player Profile (username, playerName, email, password)
    @PutMapping("/{playerId}/profile")
    public ResponseEntity<Map<String, Object>> updatePlayerProfile(
        @PathVariable Long playerId,
        @RequestBody PlayerProfileUpdateRequest updateRequest
    ) {
        try {
            Player player = playerService.updatePlayerProfile(playerId, updateRequest);
            return ResponseUtil.createSuccessResponse("Profile updated successfully.", player);
        } catch (PlayerNotFoundException e) {
            return ResponseUtil.createNotFoundResponse("Player not found.");
        } catch (Exception e) {
            logger.error("Error occurred while updating player profile: {}", playerId, e);
            return ResponseUtil.createInternalServerErrResponse("Error saving profile.");
        }
    }

    @GetMapping("/{playerId}/profile-picture")
    public ResponseEntity<Map<String, Object>> getPlayerProfilePicture(@PathVariable Long playerId) {
        try {
            String profilePictureUrl = playerService.getProfilePicture(playerId);
            if (profilePictureUrl != null) {
                return ResponseUtil.createSuccessResponse("Profile picture found.", Map.of("profilePictureUrl", profilePictureUrl));
            }
            return ResponseUtil.createNotFoundResponse("No profile picture found.");
        } catch (PlayerNotFoundException e) {
            return ResponseUtil.createNotFoundResponse("Player not found.");
        } catch (Exception e) {
            logger.error("Error fetching profile picture for player: {}", playerId, e);
            return ResponseUtil.createInternalServerErrResponse("Error fetching profile picture.");
        }
    }

    // Endpoint get top 3 played champions and player stats
    @GetMapping("/{playerId}/profile")
    public ResponseEntity<Map<String, Object>> getPlayerProfile(@PathVariable Long playerId) {
        try {
            List<Map<String, Object>> topChampions = playerService.getFormattedTopChampions(playerId);
            Map<String, Object> playerStats = playerService.getPlayerStats(playerId);

            if (topChampions == null || topChampions.isEmpty()) {
                topChampions = Collections.emptyList();
            }

            if (playerStats == null || playerStats.isEmpty()) {
                return ResponseUtil.createNotFoundResponse("No player stats found for playerId " + playerId);
            }

            playerStats.put("topChampions", topChampions);
            return ResponseUtil.createSuccessResponse("Player profile retrieved successfully.", playerStats);
        } catch (PlayerNotFoundException e) {
            return ResponseUtil.createNotFoundResponse("Player not found.");
        } catch (Exception e) {
            logger.error("Error occurred while retrieving player profile: {}", playerId, e);
            return ResponseUtil.createInternalServerErrResponse("Error retrieving player profile.");
        }
    }

    // Endpoint to get match history
    @GetMapping("/{playerId}/match-history")
    public ResponseEntity<Map<String, Object>> getPlayerMatchHistory(@PathVariable Long playerId) {
        try {
            List<Map<String, Object>> matchHistory = tournamentService.getPlayerMatchHistory(playerId);
            if (matchHistory.isEmpty()) {
                return ResponseUtil.createNotFoundResponse("No match history found.");
            }
            return ResponseUtil.createSuccessResponse("Match history retrieved successfully.", matchHistory);
        } catch (Exception e) {
            logger.error("Error occurred while fetching match history: {}", playerId, e);
            return ResponseUtil.createInternalServerErrResponse("Error fetching match history.");
        }
    }


    // Endpoint to get player's rank name
    @GetMapping("/{playerId}/rank")
    public ResponseEntity<Map<String, Object>> getPlayerRank(@PathVariable Long playerId) {
        try {
            String rankName = playerStatsService.getPlayerRankName(playerId);
            logger.error("RANKNAMEEEE: {}", rankName);
            return ResponseUtil.createSuccessResponse("Player rank retrieved successfully.", Map.of("rankName", rankName));
        } catch (Exception e) {
            logger.error("Error occurred while fetching player's rank: {}", playerId, e);
            return ResponseUtil.createInternalServerErrResponse("Error fetching player rank.");
        }
    }   
}
