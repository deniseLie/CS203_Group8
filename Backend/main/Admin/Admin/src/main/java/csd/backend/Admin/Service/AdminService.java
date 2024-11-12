package csd.backend.Admin.Service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import csd.backend.Admin.Model.*;
import csd.backend.Admin.Repository.*;

@Service
public class AdminService {

    private final UserRepository userRepository; //PLAYER ADMIN ACTION
    private final MatchRepository matchRepository; // Match ADMIN FUNCT
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(UserRepository userRepository, MatchRepository matchRepository) {
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Initialize the password encoder
    }
    
    // Method to authenticate user using username and password
    public String authenticateUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return "User authorized";
            }
        }
        return "User unauthorized";
    }

    // Method get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Create a new user with role validation
    public String createUser(String username, String password, String role) {
        if (userRepository.existsByUsername(username)) {
            return "User already exists";
        }

        // Validate role
        if (!role.equalsIgnoreCase("player") && !role.equalsIgnoreCase("admin")) {
            return "Invalid role. Allowed roles are 'player' or 'admin'.";
        }

        // Hash the password
        String hashedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setRole(role);
        
        // Save the user to the repository
        userRepository.save(user);

        return "User created successfully";
    }

    // Create new user with Id - from SQS
    public String createUserWithId(User newUser) {
        if (userRepository.existsById(newUser.getId())) {
            return "User already exists";
        }
    
        // Hash the password before saving the user (if needed)
        String hashedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);
    
        // Save the user to the database
        userRepository.save(newUser);

        return "User created successfully";
    }

    // Delete user by userId
    public String deleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        
        if (userOptional.isPresent()) {
            userRepository.deleteById(userId);
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
    
    // not necessary.. i think..
    // public String deleteMatch(int matchId) {
    //     if (matchRepository.existsById(matchId)) {
    //         matchRepository.deleteById(matchId);
    //         return "Match deleted successfully";
    //     } else {
    //         return "Match not found";
    //     }
    // }
    
}
