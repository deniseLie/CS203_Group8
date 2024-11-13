package com.loltournament.loginservice.controller;

import com.loltournament.loginservice.exception.UserNotFoundException;
import com.loltournament.loginservice.model.Player;
import com.loltournament.loginservice.security.SecurityConfig;
import com.loltournament.loginservice.util.JwtUtil;
import com.loltournament.loginservice.service.PlayerService;
import com.loltournament.loginservice.service.SqsService;
// import com.loltournament.loginservice.exception.UserNotFoundException;
// import com.loltournament.loginservice.exception.InvalidCredentialsException;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
// import org.springframework.security.oauth2.core.user.OAuth2User;
// import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * AuthController handles authentication-related operations such as user
 * registration and login.
 * It uses Spring Security for authentication and JWT for token generation.
 */
@RestController
@RequestMapping("/auth") // Base URL for all endpoints in this controller
public class AuthController {

    // Logger for this class
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PlayerService userService;

    @Autowired
    private SqsService sqsService;

    /**
     * Endpoint to register a new user with LOCAL authentication provider.
     * The user submits their username, password, email, and playername.
     * 
     * @param user Player object containing registration information
     * @return ResponseEntity with a message indicating success or failure
     */
    @PostMapping(value = "/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Player player) {
        try {
            // Encode the password before saving the user
            player.setPassword(new SecurityConfig().passwordEncoder().encode(player.getPassword()));
            Long playerId = userService.saveUser(player); // Save the user to the database

            // Prepare a success response
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "User registered successfully!");

            String messageBody = "{\"playerId\": \"" + playerId + "\", \"email\": \"" + player.getEmail() + "\"}";
            String messageGroupId = "player-" + playerId;
            String actionType = "addPlayer";
            sqsService.sendMessageToQueue(sqsService.accountQueueUrl, messageBody, messageGroupId, actionType);
            sqsService.sendMessageToQueue(sqsService.matchmakingQueueUrl, messageBody, messageGroupId, actionType);
            sqsService.sendMessageToQueue(sqsService.penaltyQueueUrl, messageBody, messageGroupId, actionType);
            sqsService.sendMessageToQueue(sqsService.adminQueueUrl, messageBody, messageGroupId, actionType);

            return new ResponseEntity<>(responseBody, HttpStatus.OK);

            // MISSING:
            // Should add exceptions for if email/username is already taken
        } catch (Exception e) {
            e.printStackTrace();

            // Prepare an error response
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error registering user");
            errorResponse.put("error", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to log in an existing user.
     * 
     * @param user Player object containing login information
     * @return ResponseEntity with the JWT token if login is successful
     */
    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Player user) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            // Load user details after successful authentication
            UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
            if (userDetails == null) {
                throw new RuntimeException("Invalid username or password");
            }
            // Generate JWT token for the authenticated user
            // Assuming that your UserDetails contains 'playername' and 'userId'
            String playername = ((Player) userDetails).getPlayername();  // Assuming Player has 'getPlayername()' method
            Long userId = ((Player) userDetails).getUserId();  // Assuming Player has 'getUserId()' method

            // Generate JWT token for the authenticated user
            String jwt = jwtUtil.generateToken(playername, userId);

            // Prepare a structured JSON response with the JWT token
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("jwt", jwt);

            // Put JWT token in body and also in the Authorization header
            // Can remove from Authorization header if you want to
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt) // Set the Authorization header
                    .body(responseBody);

        } catch (BadCredentialsException ex) {
            logger.error("Invalid credentials for user: {}", user.getUsername(), ex);

            // Prepare an error response
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid username or password");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse); // Return 401 Unauthorized
        } catch (Exception ex) {
            logger.error("An error occurred during login: {}", ex.getMessage(), ex);
            throw new RuntimeException("An error occurred during login", ex);
        }
    }

    /**
     * Endpoint to retrieve a user by their ID.
     *
     * @param userId The ID of the user to retrieve
     * @return ResponseEntity with user details or error message if user not found
     */
    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long userId) {
        try {
            // Load user details by ID
            UserDetails userDetails = userService.loadUserById(userId);

            // Assuming UserDetails is an instance of Player or can be cast to it
            if (userDetails instanceof Player) {
                Player player = (Player) userDetails;

                // Prepare a structured JSON response with user details
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("userId", player.getUserId());
                responseBody.put("username", player.getUsername());
                responseBody.put("email", player.getEmail());
                responseBody.put("playername", player.getPlayername());
                responseBody.put("authProvider", player.getAuthProvider());

                return ResponseEntity.ok(responseBody);
            } else {
                throw new RuntimeException("User details not found.");
            }

        } catch (UserNotFoundException ex) {
            logger.error("User with ID {} not found: {}", userId, ex.getMessage());

            // Prepare an error response
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "User not found");
            errorResponse.put("error", ex.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // Return 404 Not Found
        } catch (Exception ex) {
            logger.error("An error occurred while fetching user by ID: {}", ex.getMessage(), ex);

            // Prepare a generic error response
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "An error occurred while retrieving the user");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse); // Return 500 Internal Server Error
        }
    }
}
