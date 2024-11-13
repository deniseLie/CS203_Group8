package csd.backend.Admin.Controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import csd.backend.Admin.Model.DTO.TournamentDTO;
import csd.backend.Admin.Model.RequestBody.CreateOrUpdateRoundRequest;
import csd.backend.Admin.Model.Tournament.*;
import csd.backend.Admin.Service.*;

@RestController
@RequestMapping("/admin/tournament")
public class TournamentController {
    
    private final TournamentService tournamentService;
    private final TournamentRoundService tournamentRoundService;

    @Autowired
    public TournamentController(TournamentService tournamentService, TournamentRoundService tournamentRoundService) {
        this.tournamentService = tournamentService;
        this.tournamentRoundService = tournamentRoundService;
    }

    // Get all tournaments with details
    @GetMapping("/getAllTournaments")
    public List<TournamentDTO> getAllTournaments() {
        return tournamentService.getAllTournamentsDTO();
    }

    // Endpoint to create or update a tournament round
    @PostMapping("/round")
    public ResponseEntity<Map<String, Object>> createOrUpdateRound(@RequestBody CreateOrUpdateRoundRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Call service to process the round
            String result = tournamentRoundService.createOrUpdateTournamentRound(
                    request.getTournamentId(),
                    request.getFirstPlayerId(),
                    request.getSecondPlayerId(),
                    request.getWinnerPlayerId(),
                    request.getRoundNumber()
            );

            // Success response
            response.put("message", result);
            return new ResponseEntity<>(response, HttpStatus.OK);  // OK response for successful operation
        } catch (Exception e) {
            // Error handling
            response.put("message", "Error occurred while processing the round.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  // 500 status for errors
        }
    }

    // Endpoint to update tournament size
    @PostMapping("/updateTournamentSize")
    public ResponseEntity<Map<String, Object>> updateTournamentSize(@RequestBody int newTournamentSize) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Call service to update tournament size
            String result = tournamentService.updateTournamentSize(newTournamentSize);
            
            // Success response
            response.put("message", result);
            return new ResponseEntity<>(response, HttpStatus.OK);  // OK response for successful operation
        } catch (Exception e) {
            // Error handling
            response.put("message", "Error occurred while updating the tournament size.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  // 500 status for errors
        }
    }

    //MATCH ADMIN ACTIONS
    
    //may need to keep
    // @PostMapping("/create")
    // public Tournament createTournament(@RequestParam int matchId) {
    //     Tournament tournament = new Tournament();
    //     return userService.createTournament(tournament);
    // }

    // @Autowired
    // private TournamentResultService tournamentResultService;

    // @GetMapping("/tournamentResult/{tournamentResultId}")
    // public ResponseEntity<?> getTournamentResultById(@PathVariable int tournamentResultId) {
    //     TournamentResult tournamentResult = tournamentResultService.getTournamentResultByTournamentId(tournamentResultId);
    //     if (tournamentResult != null) {
    //         return ResponseEntity.ok(tournamentResult);
    //     } else {
    //         return ResponseEntity.status(404).body("Tournament result not found");
    //     }
    // }

    // @PostMapping("/tournamentResult")
    // public ResponseEntity<TournamentResult> createTournamentResult(@RequestBody TournamentResultRequest request) {
    //     TournamentResult newTournamentResult = tournamentResultService.createTournamentResult(request);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(newTournamentResult);
    // }
}
