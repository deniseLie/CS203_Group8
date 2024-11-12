package csd.backend.Account.MS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;

import csd.backend.Account.MS.DTO.PlayerProfileUpdateRequest;
import csd.backend.Account.MS.Model.Player.*;
import csd.backend.Account.MS.Model.Tournament.*;
import csd.backend.Account.MS.Service.Tournament.*;
import csd.backend.Account.MS.Service.Player.*;
import csd.backend.Account.MS.Service.Champion.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final PlayerService playerService;
    private final TournamentService tournamentService;
    private final ChampionService championService;

    @Autowired
    public AccountController(PlayerService playerService, TournamentService tournamentService, ChampionService championService) {
        this.playerService = playerService;
        this.tournamentService = tournamentService;
        this.championService = championService;
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

    // Endpoint get top 3 played champions and player stats
    @GetMapping("/{playerId}/profile")
    public ResponseEntity<Map<String, Object>> getPlayerProfile(@PathVariable Long playerId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Get the top 3 played champions
            List<PlayerChampionStats> topChampions = playerService.getTop3PlayedChampions(playerId);

            // Get player stats
            Map<String, Object> playerStats = playerService.getPlayerStats(playerId);

            if (!topChampions.isEmpty() && !playerStats.isEmpty()) {
                
                // Combine the results into a response map
                playerStats.put("topChampions", topChampions);
                response.putAll(playerStats);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "No data found for the player");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put("error", "An error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to get match history
    @GetMapping("/{playerId}/match-history")
    public ResponseEntity<List<Map<String, Object>>> getPlayerMatchHistory(@PathVariable Long playerId) {
        List<Map<String, Object>> response = new ArrayList<>();

        try {
            // Get player match history via PlayerService
            Map<String, Object> matchHistory = new HashMap<>();
            matchHistory.put("matchHistory", tournamentService.getPlayerMatchHistory(playerId));

            // Return the response as formatted JSON
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and send error response
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while fetching the match history: " + e.getMessage());
            return new ResponseEntity<>(List.of(errorResponse), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
