package csd.backend.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    //PLAYER ADMIN ACTIONS
    //to do: add jwt
    @PostMapping("/authenticate")
    public String authenticate(@RequestParam String username, @RequestParam String password) {
        return adminService.authenticateUser(username, password);
    }
    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }
    @PostMapping("/createUser")
    public String createUser(@RequestParam String username, @RequestParam String password) {
        return adminService.createUser(username, password);
    }
    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam String username) {
        return adminService.deleteUser(username);
    }

    //MATCH ADMIN ACTIONS
    @GetMapping("/getAllMatches")
    public List<Match> getAllMatches() {
        return adminService.getAllMatches();
    }
    @PostMapping("/create")
    public Match createMatch(@RequestParam int matchId) {
        Match match = new Match();
        return adminService.createMatch(match);
    }
}

