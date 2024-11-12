package csd.backend.Penalty.MS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/penalty")
public class PenaltyController {

    private static final Logger logger = LoggerFactory.getLogger(PenaltyController.class);
    
    @Autowired
    private PenaltyService penaltyService;

    // API endpoint to ban a player for a specified duration
    @PostMapping("/ban")
    public ResponseEntity<Map<String, Object>> banPlayer(@RequestParam String playerId) {
        
        Map<String, Object> response = new HashMap<>();
        try {
            penaltyService.banPlayer(playerId);
            response.put("message", "Player banned successfully.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while banning player: {}", playerId, e);
            response.put("message", "Error banning player.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API endpoint to check the status of a player
    @GetMapping("/check-status")
    public ResponseEntity<Map<String, Object>> checkPlayerStatus(@RequestParam String playerId) {
        logger.info("Checking status for player: {}", playerId);
        Map<String, Object> playerStatus = penaltyService.checkPlayerStatus(playerId);

        if (playerStatus != null) {
            return new ResponseEntity<>(playerStatus, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Player not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
