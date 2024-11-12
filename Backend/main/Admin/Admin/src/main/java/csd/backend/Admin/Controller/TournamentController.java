package csd.backend.Admin.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import csd.backend.Admin.Service.*;

@RestController
@RequestMapping("/admin/tournament")
public class TournamentController {
    
    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private TournamentRoundService tournamentRoundService;

    // Endpoint to create or update a tournament round
    @PutMapping("/round")
    public String createOrUpdateRound(
            @RequestParam Long tournamentId,
            @RequestParam Long firstPlayerId,
            @RequestParam Long secondPlayerId,
            @RequestParam Long winnerPlayerId,
            @RequestParam int roundNumber) {

        // Call service to process the round
        String result = tournamentRoundService.createOrUpdateTournamentRound(tournamentId, firstPlayerId, secondPlayerId, winnerPlayerId, roundNumber);

        return result; // Response message indicating success or failure
    }
}
