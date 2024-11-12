package csd.backend.Account.MS.Service.Player;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import csd.backend.Account.MS.DTO.PlayerProfileUpdateRequest;
import csd.backend.Account.MS.Exception.*;
import csd.backend.Account.MS.Model.Player.*;
import csd.backend.Account.MS.Repository.Player.*;
import csd.backend.Account.MS.Service.*;
import csd.backend.Account.MS.Service.Champion.ChampionService;
import jakarta.transaction.Transactional;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import org.mindrot.jbcrypt.BCrypt;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerOverallStatsRepository playerOverallStatsRepository;
    private final PlayerChampionStatsRepository playerChampionStatsRepository;
    private final PlayerStatsService playerStatsService;
    private final SqsService sqsService;
    private final ChampionService championService;  
    private final PasswordEncoder passwordEncoder; 

    // Constructor-based dependency injection
    @Autowired
    public PlayerService(
        PlayerRepository playerRepository,
        PlayerOverallStatsRepository playerOverallStatsRepository,
        PlayerChampionStatsRepository playerChampionStatsRepository,
        PlayerStatsService playerStatsService,
        SqsService sqsService,
        ChampionService championService,
        PasswordEncoder passwordEncoder
    ) {
        this.playerRepository = playerRepository;
        this.playerOverallStatsRepository = playerOverallStatsRepository;
        this.playerChampionStatsRepository = playerChampionStatsRepository;
        this.playerStatsService = playerStatsService;
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

    // Handle match completion and recalculate stats
    @Transactional
    public void handleMatchCompletion(Long playerId, Long championId, double kdRate, int finalPlacement, int rankPoints, boolean isWin) {
        // Check if player already exists
        if (!playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException(playerId);
        }

        // Update Player stats in overall stats and champion stats
        playerStatsService.updateOverallStats(playerId, rankPoints, kdRate, finalPlacement, isWin);
        playerStatsService.updateChampionStats(playerId, championId, kdRate, finalPlacement, isWin);
    }

    // New method to format top champions
    public List<Map<String, Object>> getFormattedTopChampions(Long playerId) {
        // Retrieve the top 3 played champions
        List<PlayerChampionStats> topChampions = getTop3PlayedChampions(playerId);

        // Format the top champions data
        return topChampions.stream()
            .map(championStats -> {
                Map<String, Object> championData = new HashMap<>();
                championData.put("championId", championStats.getChampionId());

                // Get champion name using the ChampionService
                String championName = championService.getChampionById(championStats.getChampionId()).getChampionName();
                championData.put("championName", championName);

                // Add other attributes
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
        // Check if the player exists
        if (!playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException(playerId);
        }

        Optional<PlayerOverallStats> playerStatsOpt = Optional.ofNullable(playerOverallStatsRepository.findByPlayerId(playerId));
        Map<String, Object> statsMap = new HashMap<>();

        if (playerStatsOpt.isPresent()) {
            PlayerOverallStats stats = playerStatsOpt.get();
            double firstPlacePercentage = (double) stats.getTotalFirstPlaceMatches() / stats.getTotalNumberOfMatches() * 100;

            // Return player stats as a map
            statsMap.put("totalMatches", stats.getTotalNumberOfMatches());
            statsMap.put("averagePlace", stats.getOverallAveragePlace());
            statsMap.put("firstPlacePercentage", firstPlacePercentage);

        } else {
            statsMap.put("message", "Player stats not found");
        }
    
        return statsMap;
    }

    // Save profile picture as a BLOB
    public void saveProfilePicture(Long playerId, MultipartFile file) throws IOException {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));

        // Convert the image file to a byte array
        byte[] imageBytes = file.getBytes();

        // Set the profile picture as a BLOB
        player.setProfilePicture(imageBytes);

        // Save the player object with the profile picture
        playerRepository.save(player);
    }

    // Get Profile Picture 
    public byte[] getProfilePicture(Long playerId) {
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
        byte[] profilePicture = updateRequest.getProfilePicture();

        // Retrieve player
        Player player = getPlayerById(playerId);

        // Update the username if provided
        if (username != null && !username.isEmpty()) {
            player.setUsername(username);
        }

        // Handle profile picture upload if provided
        if (profilePicture != null && profilePicture.length > 0) {
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
            String messageBody = prepareMessageBody(playerId, playerName, email, password);
            
            // Define the message attributes
            Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
            messageAttributes.put("actionType", MessageAttributeValue.builder().stringValue("updatePlayerProfile").build());

            // Send the message to the SQS queue
            // sqsService.sendMessageToQueue("login", messageBody, messageAttributes);
        }
        return player;
    }

    // Helper method to hash the password using bcrypt
    private String hashPassword(String password) {
        // Generate a salt
        String salt = BCrypt.gensalt();
        
        // Hash the password using the salt
        return BCrypt.hashpw(password, salt);
    }

    // Helper method to prepare the message body for SQS
    private String prepareMessageBody(Long playerId, String playerName, String email, String password) {
        // Create a JSON-like structure for the message body
        Map<String, String> messageData = new HashMap<>();
        messageData.put("playerId", playerId.toString());
        if (playerName != null) messageData.put("playerName", playerName);
        if (email != null) messageData.put("email", email);
        if (password != null) messageData.put("password", password);

        // Convert the map into a string (you can also use a JSON library like Jackson for this)
        return messageData.toString();
    }

    // Helper method to prepare the message body for SQS with updated username
    private String prepareUsernameMessageBody(Long playerId, String username) {
        Map<String, String> messageData = new HashMap<>();
        messageData.put("playerId", playerId.toString());
        messageData.put("username", username); 

        return messageData.toString(); 
    }
}
