package csd.backend.Penalty.MS;

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
    public String banPlayer(@RequestParam String playerId) {
        logger.info("Received request to ban player: {}, Duration: {} minutes", playerId);
        try {
            penaltyService.banPlayer(playerId);
            return "Player banned successfully";
        } catch (Exception e) {
            logger.error("Error occurred while banning player: {}", playerId, e);
            return "Error banning player.";
        }
    }

    // API endpoint to check the status of a player
    @GetMapping("/check-status")
    public Map<String, Object> checkPlayerStatus(@RequestParam String playerId) {
        logger.info("Checking status for player: {}", playerId);
        return penaltyService.checkPlayerStatus(playerId);
    }
}
