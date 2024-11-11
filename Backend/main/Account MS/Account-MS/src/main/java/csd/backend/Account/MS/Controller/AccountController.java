package csd.backend.Account.MS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Service;

import csd.backend.Account.MS.Model.Player.*;
import csd.backend.Account.MS.Model.Tournament.*;
import csd.backend.Account.MS.Service.Tournament.*;
import csd.backend.Account.MS.Service.Player.*;
import csd.backend.Account.MS.Service.Champion.*;

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
            // Get all TournamentPlayerStats for the player
            List<TournamentPlayerStats> playerTournamentStats = tournamentService.getTournamentPlayerStatsForPlayer(playerId);

            // Collect all tournamentIds
            List<Long> tournamentIds = playerTournamentStats.stream()
                    .map(tournamentPlayerStats -> tournamentPlayerStats.getTournament().getTournamentId())
                    .distinct()
                    .collect(Collectors.toList());

            // Loop through tournamentIds and gather data
            for (Long tournamentId : tournamentIds) {
                Map<String, Object> tournamentData = new HashMap<>();
                List<TournamentPlayerStats> statsForTournament = tournamentService.getTournamentPlayerStats(tournamentId);

                // Fetch tournament details from the Tournament entity
                Tournament tournament = tournamentService.getTournamentById(tournamentId);
                LocalDateTime tournamentStart = tournament.getTimestampStart();

                tournamentData.put("tournamentId", tournamentId);
                tournamentData.put("tournamentSize", tournament.getTournamentSize());
                tournamentData.put("timestampStart", tournamentStart.toString());

                List<Map<String, Object>> matchDetails = new ArrayList<>();

                // For each player's stats in the tournament
                for (TournamentPlayerStats stats : statsForTournament) {
                    Map<String, Object> matchDetail = new HashMap<>();
                    matchDetail.put("tournamentPlayerId", stats.getTournamentPlayerId());

                    // Build player data in the desired format
                    Map<String, Object> playerData = new HashMap<>();
                    playerData.put("standing", stats.getFinalPlacement());
                    playerData.put("champion", championService.getChampionById(stats.getChampionPlayedId()).getChampionName());
                    playerData.put("playerName", stats.getPlayer().getUsername());
                    playerData.put("kd", stats.getKillCount() + "/" + stats.getDeathCount());
                    playerData.put("kda", (stats.getDeathCount() == 0) ? "Infinity" : String.format("%.2f KDA", (float) stats.getKillCount() / stats.getDeathCount()));
                    playerData.put("lpChange", stats.getPointObtain());

                    // Format timeEndPerPlayer to date and time
                    LocalDateTime timeEnd = stats.getTimeEndPerPlayer();
                    playerData.put("time", timeEnd != null ? timeEnd.toLocalTime().toString() : "N/A");
                    playerData.put("date", timeEnd != null ? timeEnd.toLocalDate().toString() : "N/A");
                    playerData.put("isAFK", stats.getIsAFK() ? true : false);
                    matchDetails.add(playerData);
                }

                // Add matchDetails to tournamentData
                tournamentData.put("players", matchDetails);
                response.add(tournamentData);
            }

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
