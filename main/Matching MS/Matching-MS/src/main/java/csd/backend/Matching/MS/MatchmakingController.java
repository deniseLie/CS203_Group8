package csd.backend.Matching.MS;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/matchmaking")
public class MatchmakingController {
    
    @Autowired
    private MatchmakingService matchmakingService;

    @PostMapping("/join")
    public String joinMatchmaking(@RequestParam String playerName, @RequestParam String email) {
        int rankId = 1;

        // Add player to matchmaking pool
        matchmakingService.addPlayerToPool(playerName, email, "queue", rankId);

        // Check if enough players are available for a match
        List<Map<String, AttributeValue>> players = matchmakingService.checkPlayersInQueue(rankId);

        if (players.size() >= 8) {
            // If enough players, create a match and remove them from the queue
            matchmakingService.createMatch(players);
            matchmakingService.removePlayersFromQueue(players);
            return "Match started with players: " + players;
        }

        return "Waiting for more players ... Current pool size: " + players.size();
    }

    // Endpoint to trigger SQS processing manually (for testing)
    @PostMapping("/processSqs")
    public String processSqsMessages() {
        matchmakingService.processSqsMessages();
        return "SQS Messages processed!";
    }
    
}
