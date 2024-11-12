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
    // @GetMapping("/getAllMatches")
    // public List<Match> getAllMatches() {
    //     return userService.getAllMatches();
    // }
    //may need to keep
    // @PostMapping("/create")
    // public Match createMatch(@RequestParam int matchId) {
    //     Match match = new Match();
    //     return userService.createMatch(match);
    // }

    @Autowired
    private MatchResultService matchResultService;

    @GetMapping("/matchresult/{matchresultId}")
    public ResponseEntity<?> getMatchResultById(@PathVariable int matchresultId) {
        MatchResult matchResult = matchResultService.getMatchResultByTournamentId(matchresultId);
        if (matchResult != null) {
            return ResponseEntity.ok(matchResult);
        } else {
            return ResponseEntity.status(404).body("Match result not found");
        }
    }

    @PostMapping("/matchresult")
    public ResponseEntity<MatchResult> createMatchResult(@RequestBody MatchResultRequest request) {
        MatchResult newMatchResult = matchResultService.createMatchResult(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMatchResult);
    }
}

