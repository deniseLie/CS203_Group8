package csd.backend.Admin.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import csd.backend.Admin.Model.*;
import csd.backend.Admin.Service.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    //PLAYER ADMIN ACTIONS
    //to do: add jwt
    @PostMapping("/authenticate")
    public String authenticate(@RequestParam String username, @RequestParam String password) {
        return userService.authenticateUser(username, password);
    }
    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping("/createUser")
    public String createUser(@RequestParam String username, @RequestParam String password, @RequestParam String role) {
        return userService.createUser(username, password, role);
    }
    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam Long userId) {
        return userService.deleteUser(userId);
    }

    //MATCH ADMIN ACTIONS
    //remove in next commit
    // @GetMapping("/getAllTournaments")
    // public List<Tournament> getAllTournaments() {
    //     return userService.getAllTournaments();
    // }
    //may need to keep
    // @PostMapping("/create")
    // public Tournament createTournament(@RequestParam int matchId) {
    //     Tournament tournament = new Tournament();
    //     return userService.createTournament(tournament);
    // }

    @Autowired
    private TournamentResultService tournamentResultService;

    @GetMapping("/tournamentResult/{tournamentResultId}")
    public ResponseEntity<?> getTournamentResultById(@PathVariable int tournamentResultId) {
        TournamentResult tournamentResult = tournamentResultService.getTournamentResultByTournamentId(tournamentResultId);
        if (tournamentResult != null) {
            return ResponseEntity.ok(tournamentResult);
        } else {
            return ResponseEntity.status(404).body("Tournament result not found");
        }
    }

    @PostMapping("/tournamentResult")
    public ResponseEntity<TournamentResult> createTournamentResult(@RequestBody TournamentResultRequest request) {
        TournamentResult newTournamentResult = tournamentResultService.createTournamentResult(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTournamentResult);
    }
}

