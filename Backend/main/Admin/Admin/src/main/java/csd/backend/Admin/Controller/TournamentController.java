package csd.backend.Admin.Controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import csd.backend.Admin.Model.DTO.TournamentDTO;
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

    @GetMapping("/getAllTournaments")
    public List<TournamentDTO> getAllTournaments() {
        return tournamentService.getAllTournamentsDTO();
    }

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
