package csd.backend.Matching.MS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/matchmaking")
public class PenaltyController {

    private static final Logger logger = LoggerFactory.getLogger(PenaltyController.class);
    
    @Autowired
    private PenaltyService penaltyService;

    // API endpoint to ban a player for a specified duration
    @PostMapping("/ban")
    public String banPlayer(@RequestParam String playerName, @RequestParam int duration) {
        logger.info("Received request to ban player: {}, Duration: {} minutes", playerName, duration);
        try {
            penaltyService.banPlayer(playerName, duration);
            return "Player banned successfully for " + duration + " minutes.";
        } catch (Exception e) {
            logger.error("Error occurred while banning player: {}", playerName, e);
            return "Error banning player.";
        }
    }

    // API endpoint to check the status of a player
    @GetMapping("/check-status")
    public Map<String, Object> checkPlayerStatus(@RequestParam String playerName) {
        logger.info("Checking status for player: {}", playerName);
        return penaltyService.checkPlayerStatus(playerName);
    }

    // Endpoint to trigger SQS processing manually (for testing)
    @PostMapping("/processSqs")
    public String processSqsMessages() {
        logger.info("Processing SQS messages...");
        try {
            matchmakingService.processSqsMessages();
            logger.info("SQS Messages processed successfully.");
            return "SQS Messages processed!";
        } catch (Exception e) {
            logger.error("Error occurred while processing SQS messages", e);
            return "Error processing SQS messages.";
        }
    }
}
