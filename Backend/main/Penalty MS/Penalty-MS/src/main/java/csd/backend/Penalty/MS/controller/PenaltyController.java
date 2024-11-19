package csd.backend.Penalty.MS.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import csd.backend.Penalty.MS.service.PenaltyService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/penalty")
public class PenaltyController {

    private static final Logger logger = LoggerFactory.getLogger(PenaltyController.class);
    
    @Autowired
    private PenaltyService penaltyService;

    /**
     * API endpoint to ban a player for a specified duration based on their previous ban count.
     * 
     * @param playerId - The ID of the player to be banned.
     * @return ResponseEntity - Contains a message indicating whether the player was banned successfully
     *         or if there was an error (e.g., 500 Internal Server Error).
     * 
     * Logic:
     * - This method calls the banPlayer method of PenaltyService to perform the banning action.
     * - If the operation is successful, a success message is returned with a 200 OK status.
     * - In case of failure (e.g., unexpected error during banning), an error message is returned with a 500 Internal Server Error.
     */
    @PostMapping("/ban")
    public ResponseEntity<Map<String, Object>> banPlayer(@RequestParam Long playerId) {
        return penaltyService.banPlayer(playerId);
    }

    /**
     * API endpoint to check the current ban status of a player.
     * 
     * @param playerId - The ID of the player whose ban status is to be checked.
     * @return ResponseEntity - Contains the player's ban status information (playerId, queueStatus, remainingTime) 
     *         or an error message if the player is not found (404 Not Found).
     * 
     * Logic:
     * - This method calls the checkPlayerBanStatus method of PenaltyService to fetch the player's ban status.
     * - If the player is found, their ban status (playerId, queueStatus, and remainingTime) is returned with a 200 OK status.
     * - If the player is not found (e.g., invalid playerId), a 404 Not Found status with an appropriate error message is returned.
     */
    @GetMapping("/check-status")
    public ResponseEntity<Map<String, Object>> checkPlayerBanStatus(@RequestParam Long playerId) {
        return penaltyService.checkPlayerBanStatus(playerId);
    }
}
