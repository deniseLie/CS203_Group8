package csd.backend.Account.MS.service.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import csd.backend.Account.MS.DTO.PlayerProfileUpdateRequest;
import csd.backend.Account.MS.exception.player.PlayerChampionStatsNotFoundException;
import csd.backend.Account.MS.exception.player.PlayerNotFoundException;
import csd.backend.Account.MS.exception.player.PlayerRegisterExisted;
import csd.backend.Account.MS.model.champion.Champion;
import csd.backend.Account.MS.model.player.*;
import csd.backend.Account.MS.repository.player.*;
import csd.backend.Account.MS.service.*;
import csd.backend.Account.MS.service.champion.ChampionService;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerOverallStatsRepository playerOverallStatsRepository;
    private final PlayerChampionStatsRepository playerChampionStatsRepository;
    private final SqsService sqsService;
    private final ChampionService championService;  
    private final PasswordEncoder passwordEncoder; 

    // Constructor-based dependency injection
    @Autowired
    public PlayerService(
        PlayerRepository playerRepository,
        PlayerOverallStatsRepository playerOverallStatsRepository,
        PlayerChampionStatsRepository playerChampionStatsRepository,
        SqsService sqsService,
        ChampionService championService,
        PasswordEncoder passwordEncoder
    ) {
        this.playerRepository = playerRepository;
        this.playerOverallStatsRepository = playerOverallStatsRepository;
        this.playerChampionStatsRepository = playerChampionStatsRepository;
        this.sqsService = sqsService;
        this.championService = championService;
        this.passwordEncoder = passwordEncoder;
    } 

    // Method to retrieve player by ID
    public Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));
    }

    // Method to save player after profile update
    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    // Insert new user into database
    public void registerUser(Player player) {
        // Check if player already exists
        if (playerRepository.findByUsername(player.getUsername()).isPresent()) {
            throw new PlayerRegisterExisted(player.getUsername());
        }
        playerRepository.save(player);
    }
    
    // New method to format top champions
    public List<Map<String, Object>> getFormattedTopChampions(Long playerId) {
        List<PlayerChampionStats> topChampions = getTop3PlayedChampions(playerId);
        
        // Check if topChampions is null or empty
        if (topChampions == null || topChampions.isEmpty()) {
            return Collections.emptyList();
        }

        return topChampions.stream()
            .map(championStats -> {
                Map<String, Object> championData = new HashMap<>();
                championData.put("championId", championStats.getChampionId());

                // Safely retrieve the champion name
                Champion champion = championService.getChampionById(championStats.getChampionId());
                if (champion != null) {
                    championData.put("championName", champion.getChampionName());
                } else {
                    championData.put("championName", "Unknown Champion"); // Handle missing champion
                }

                championData.put("averagePlace", championStats.getAveragePlace());
                championData.put("kdRate", championStats.getKdRate());
                championData.put("totalWins", championStats.getTotalWins());
                championData.put("totalMatchNumber", championStats.getTotalMatchNumber());

                return championData;
            })
            .collect(Collectors.toList());
    }


    // Get the top 3 played champions for the player
    public List<PlayerChampionStats> getTop3PlayedChampions(Long playerId) {
        // Check if the player exists
        if (!playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException(playerId);
        }

        List<PlayerChampionStats> championStats = playerChampionStatsRepository.findByPlayerId(playerId);

        // Sort by total matches played, descending, limit to top 3
        if (championStats != null && !championStats.isEmpty()) {
            return championStats.stream()
                    .sorted(Comparator.comparingInt(PlayerChampionStats::getTotalMatchNumber).reversed())
                    .limit(3)
                    .collect(Collectors.toList());
        
        // Handle case where no data found
        } else {
            throw new PlayerChampionStatsNotFoundException(playerId);
        }
    }

    // Get player statistics (total matches, average place, first place percentage)
    public Map<String, Object> getPlayerStats(Long playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException(playerId);
        }
    
        Optional<PlayerOverallStats> playerStatsOpt = Optional.ofNullable(playerOverallStatsRepository.findByPlayerId(playerId));
        Map<String, Object> statsMap = new HashMap<>();
    
        if (playerStatsOpt.isPresent()) {
            PlayerOverallStats stats = playerStatsOpt.get();
            int totalMatches = stats.getTotalNumberOfMatches();
            double firstPlacePercentage = totalMatches > 0 ? (double) stats.getTotalFirstPlaceMatches() / totalMatches * 100 : 0;
    
            statsMap.put("totalMatches", totalMatches);
            statsMap.put("averagePlace", stats.getOverallAveragePlace());
            statsMap.put("firstPlacePercentage", firstPlacePercentage);
        } else {
            statsMap.put("message", "Player stats not found");
            statsMap.put("totalMatches", 0);
            statsMap.put("averagePlace", 0.0);
            statsMap.put("firstPlacePercentage", 0.0);
        }
    
        return statsMap;
    }
    

    // Get Profile Picture 
    public String getProfilePicture(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));
    
        // Return the profile picture as a byte array
        return player.getProfilePicture();
    }

    // Update player profile
    public Player updatePlayerProfile(Long playerId, PlayerProfileUpdateRequest updateRequest) {
        String username = updateRequest.getUsername();
        String playerName = updateRequest.getPlayerName();
        String email = updateRequest.getEmail();
        String password = updateRequest.getPassword();
        String profilePicture = updateRequest.getProfilePicture();

        // Retrieve player
        Player player = getPlayerById(playerId);

        // Update the username if provided
        if (username != null && !username.isEmpty()) {
            player.setUsername(username);
        }

        // Handle profile picture upload if provided
        if (profilePicture != null && !profilePicture.isEmpty()) {
            player.setProfilePicture(profilePicture);
        }

        // Save the updated player
        savePlayer(player);

        // If additional data is provided, send a message to the SQS queue
        if (playerName != null || email != null || password != null) {

            // Hash the password before sending it to the SQS queue
            if (password != null) {
                password = passwordEncoder.encode(password); // Hash the password
            }

            // Prepare the message body to be sent to the SQS queue
            String messageBody = prepareMessageBody(playerId, username, playerName, email, password);
            
            // Define the message attributes
            Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
            messageAttributes.put("actionType", MessageAttributeValue.builder().stringValue("updatePlayerProfile").build());

            // Send the message to the SQS queue
            sqsService.sendMessageToQueue("login", messageBody, messageAttributes);
        }
        return player;
    }
    
    // Method to delete a player by playerId
    public void deletePlayerByPlayerId(Long playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException(playerId);
        }

        playerRepository.deleteByPlayerId(playerId);  // This will delete the player with the given playerId
    }
    

    // Helper method to prepare the message body for SQS
    private String prepareMessageBody(Long playerId, String username, String playerName, String email, String password) {
        // Create a JSON-like structure for the message body
        Map<String, String> messageData = new HashMap<>();
        messageData.put("playerId", playerId.toString());
        if (playerName != null) messageData.put("playerName", playerName);
        if (username != null) messageData.put("username", username);
        if (email != null) messageData.put("email", email);
        if (password != null) messageData.put("password", password);

        // Convert the map into a string (you can also use a JSON library like Jackson for this)
        return messageData.toString();
    }
}
