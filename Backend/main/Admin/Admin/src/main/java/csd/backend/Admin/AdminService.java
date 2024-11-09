package csd.backend.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    //PLAYER ADMIN ACTION
    @Autowired
    private UserRepository userRepository;
    // Match ADMIN FUNCT
    @Autowired
    private MatchRepository matchRepository;

    public String authenticateUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password)
                .map(user -> "user authorized")
                .orElse("user unauthorized");
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public String createUser(String username, String password) {

        if (userRepository.existsById(username)) {
            return "User already exists";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);  // to-do: hash the password
        userRepository.save(user);
        
        return "User created successfully";
    }
    public String deleteUser(String username) {
        if (userRepository.existsById(username)) {
            userRepository.deleteById(username);
            return "User deleted successfully";
        } else {
            return "User not found";
        }
    }

    //MATCH ADMIN ACTION
    public Match createMatch(Match match) {
        return matchRepository.save(match);
    }
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }
    public String deleteMatch(int matchId) {
        if (matchRepository.existsById(matchId)) {
            matchRepository.deleteById(matchId);
            return "Match deleted successfully";
        } else {
            return "Match not found";
        }
    }
    
}
