package csd.backend.Account.MS.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import csd.backend.Account.MS.DTO.PlayerProfileUpdateRequest;
import csd.backend.Account.MS.exception.PlayerNotFoundException;
import csd.backend.Account.MS.model.player.*;
import csd.backend.Account.MS.service.player.*;
import csd.backend.Account.MS.service.tournament.*;

import java.util.*;

@RestController
@RequestMapping("/account")
public class AccountController {

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
        Map<String, Object> response = new HashMap<>();
        try {

            // Update player 
            Player player = playerService.updatePlayerProfile(playerId, updateRequest);
            
            response.put("message", "Profile updated successfully.");
            response.put("player", player);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("error", "Error saving profile picture: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{playerId}/profile-picture")
    public ResponseEntity<Map<String, Object>> getPlayerProfilePicture(@PathVariable Long playerId) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Retrieve player
            Player player = playerService.getPlayerById(playerId);
            if (player == null) {
                response.put("error", "Player not found with ID: " + playerId);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // Get profile picture name
            String profilePicture = player.getProfilePicture();
            if (profilePicture == null || profilePicture.isEmpty()) {
                response.put("message", "No profile picture found for this player");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // Construct profile picture URL
            String profilePictureUrl = "/path/to/images/" + profilePicture; // Adjust the URL path to where images are stored

            response.put("profilePictureUrl", profilePictureUrl);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", "An error occurred while fetching the profile picture: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Endpoint get top 3 played champions and player stats
    @GetMapping("/{playerId}/profile")
    public ResponseEntity<Map<String, Object>> getPlayerProfile(@PathVariable Long playerId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Get formatted top 3 played champions
            List<Map<String, Object>> topChampions = playerService.getFormattedTopChampions(playerId);
            if (topChampions == null) {
                topChampions = Collections.emptyList();
            }

            // Get player stats
            Map<String, Object> playerStats = playerService.getPlayerStats(playerId);
            if (playerStats == null) {
                response.put("message", "No player stats found for playerId " + playerId);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            if (!topChampions.isEmpty() && !playerStats.isEmpty()) {
                // Combine the results into a response map
                playerStats.put("topChampions", topChampions);
                response.putAll(playerStats);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "No data found for the player");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (PlayerNotFoundException e) {
            response.put("error", "Player not found: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.put("error", "An error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Endpoint to get match history
    @GetMapping("/{playerId}/match-history")
    public ResponseEntity<Object> getPlayerMatchHistory(@PathVariable Long playerId) {
        try {
            // Get player match history via TournamentService
            List<Map<String, Object>> matchHistory = tournamentService.getPlayerMatchHistory(playerId);

            // Check if matchHistory is null or empty
            if (matchHistory == null || matchHistory.isEmpty()) {
                Map<String, Object> noDataResponse = new HashMap<>();
                noDataResponse.put("message", "No match history found for player " + playerId);
                return new ResponseEntity<>(noDataResponse, HttpStatus.NOT_FOUND);
            }

            // Return the match history directly
            return new ResponseEntity<>(matchHistory, HttpStatus.OK);

        } catch (Exception e) {
            // Handle exceptions and send error response
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while fetching the match history: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Endpoint to get player's rank name
    @GetMapping("/{playerId}/rank")
    public ResponseEntity<Map<String, Object>> getPlayerRank(@PathVariable Long playerId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Get player rank via PlayerService
            String rankName = playerStatsService.getPlayerRankName(playerId);

            // Return the response as formatted JSON
            response.put("rankName", rankName);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and send error response
            response.put("error", "An error occurred while fetching the player's rank: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }   
}
