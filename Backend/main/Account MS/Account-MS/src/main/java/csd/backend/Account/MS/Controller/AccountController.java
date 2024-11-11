package csd.backend.Account.MS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import csd.backend.Account.MS.Model.*;
import csd.backend.Account.MS.Model.Player.PlayerChampionStats;
import csd.backend.Account.MS.Service.*;
import java.util.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final PlayerService playerService;

    @Autowired
    public AccountController(PlayerService playerService) {
        this.playerService = playerService;
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
}
