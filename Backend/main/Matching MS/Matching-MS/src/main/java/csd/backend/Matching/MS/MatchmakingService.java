package csd.backend.Matching.MS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.*;

@Service
public class MatchmakingService {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    private final SqsService sqsService;
    private final PlayerService playerService;

    @Autowired
    public MatchmakingService(SqsService sqsService, PlayerService playerService) {
        this.sqsService = sqsService;
        this.playerService = playerService;
    }

    private static final String PLAYERS_TABLE = "Players";
    private static final String MATCHES_TABLE = "Matches";
    private static final int MAX_PLAYERS = 2;
    private static final Logger logger = LoggerFactory.getLogger(MatchmakingService.class);

    // Add a player to the matchmaking pool
    public void addPlayerToPool(String playerName, String email, String queueStatus, int rankId) {

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("playerName", AttributeValue.builder().s(playerName).build());
        item.put("email", AttributeValue.builder().s(email).build());
        item.put("queueStatus", AttributeValue.builder().s(queueStatus).build());
        item.put("rankId", AttributeValue.builder().n(String.valueOf(rankId)).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .item(item)
                .build();

        try {
            logger.info("Attempting to add player {} with item: {}", playerName, item);
            dynamoDbClient.putItem(putItemRequest);
            logger.info("Successfully added player {} to the pool", playerName);
        } catch (Exception e) {
            logger.error("Error adding player {} to pool", playerName, e);
            throw new RuntimeException("Error adding player to pool");
        }
    }

     // Retrieve player details from the database
     private Map<String, AttributeValue> getPlayerDetails(String playerId) {
        // Get player details
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(Map.of("playerId", AttributeValue.builder().s(playerId).build()))
                .build();

        GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);
        return getItemResponse.item();
    }

    // Check Player Status: If banned, return remaining ban time
    public Map<String, Object> checkPlayerStatus(String playerId) {
        Map<String, AttributeValue> player = getPlayerDetails(playerId);
        Map<String, Object> status = new HashMap<>();

        if (player != null) {
            status.put("playerId", player.get("playerId").s());
            status.put("queueStatus", player.get("queueStatus").s());
            long banUntil = Long.parseLong(player.get("banUntil").n());

            // Check if the player is still banned
            if (banUntil > System.currentTimeMillis()) {
                // Calculate remaining ban time
                long remainingTime = banUntil - System.currentTimeMillis();
                status.put("remainingTime", remainingTime);

            // Not banned
            } else {
                status.put("remainingTime", 0); 
            }
        } else {
            logger.warn("Player {} not found", playerId);
            status.put("error", "Player not found");
        }
        return status;
    }

    // Check Match : Enough Players with same rankId
    public boolean checkForMatch(Long rankId, boolean isSpeedUp) {

        // Get players queueing + same rankId
        List<Map<String, AttributeValue>> players = checkPlayersInQueue(rankId);

        // check do we have enough players
        if (players.size() >= MAX_PLAYERS) {

            // Take the first MAX_PLAYERS players and create a match
            List<Map<String, AttributeValue>> playersToMatch = players.subList(0, MAX_PLAYERS);
            createMatch(playersToMatch);
            return true;
        }
        return false;
    }

    // Get Queueing players with the same rankId
    private List<Map<String, AttributeValue>> checkPlayersInQueue(Long rankId) {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(PLAYERS_TABLE)
                .filterExpression("queueStatus = :queueStatus and rankId = :rankId")
                .expressionAttributeValues(Map.of(
                        ":queueStatus", AttributeValue.builder().s("queue").build(),
                        ":rankId", AttributeValue.builder().n(String.valueOf(rankId)).build()))
                .build();

        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
        return scanResponse.items();
    }

    // Check if there are enough players in the pool to start a match
    public List<Map<String, AttributeValue>> checkPlayersInSpeedUpQueue(Long rankId) {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(PLAYERS_TABLE)
                .filterExpression("queueStatus = :queueStatus and rankId BETWEEN :minRankId AND :maxRankId")
                .expressionAttributeValues(Map.of(
                        ":queueStatus", AttributeValue.builder().s("queue").build(),
                        ":minRankId", AttributeValue.builder().n(String.valueOf(rankId - 1)).build(),
                        ":maxRankId", AttributeValue.builder().n(String.valueOf(rankId + 1)).build()))
                .build();

        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
        return scanResponse.items();
    }
    
    // Remove players from the pool after a match is found
    public void removePlayersFromQueue(List<Map<String, AttributeValue>> players) {
        for (Map<String, AttributeValue> player : players) {
            String playerName = player.get("playerName").s();

            // Update the player's queueStatus to 'not queue' after they are matched
            Map<String, AttributeValueUpdate> updates = new HashMap<>();
            updates.put("queueStatus", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s("not queue").build())
                    .action(AttributeAction.PUT)
                    .build());

            UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                    .tableName(PLAYERS_TABLE)
                    .key(Map.of("playerName", AttributeValue.builder().s(playerName).build()))
                    .attributeUpdates(updates)
                    .build();

            dynamoDbClient.updateItem(updateRequest);
        }
    }

    public void updatePlayerBanStatus(String playerId, String queueStatus, long banEndTime) {
        
        // Update player's status to 'banned' and set the 'banUntil' timestamp
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("queueStatus", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(queueStatus).build())
                .action(AttributeAction.PUT)
                .build());
        updates.put("banUntil", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().n(String.valueOf(banEndTime)).build())
                .action(AttributeAction.PUT)
                .build());
    
        // Prepare the update request to DynamoDB
        UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(Map.of("playerId", AttributeValue.builder().s(playerId).build())) // Player key
                .attributeUpdates(updates)
                .build();
    
        // Execute the update request
        try {
            dynamoDbClient.updateItem(updateRequest);
            logger.info("Player {} has been banned until {}.", playerId, banEndTime);
        } catch (Exception e) {
            logger.error("Failed to update ban status for player {}: {}", playerId, e.getMessage());
        }
    }
    

    // Create a new match with players from the database
    public void createMatch(List<Map<String, AttributeValue>> players) {
        Map<String, AttributeValue> matchItem = new HashMap<>();
        matchItem.put("matchId", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis())).build()); // Use timestamp as matchId
    
        // Add player names to the match and update their queue status
        List<AttributeValue> playerList = new ArrayList<>();
        StringBuilder playerIds = new StringBuilder();  // StringBuilder to accumulate player IDs

        for (Map<String, AttributeValue> player : players) {
            String playerName = player.get("playerName").s();
            String playerId = player.get("playerId").n();  // Assuming playerId exists in the map
        
            // Add player name to the match player list
            playerList.add(AttributeValue.builder().s(playerName).build());
            
            // Add player ID to the comma-separated string
            if (playerIds.length() > 0) {
                playerIds.append(",");  // Add a comma between IDs
            }
            playerIds.append(String.valueOf(playerId));  // Append the player ID
            
            // Update the player status to "unqueue"
            playerService.updatePlayerStatus(playerName, "unqueue");  
        }
    
        matchItem.put("players", AttributeValue.builder().l(playerList).build());
    
        PutItemRequest matchRequest = PutItemRequest.builder()
                .tableName(MATCHES_TABLE)
                .item(matchItem)
                .build();
    
        try {
            dynamoDbClient.putItem(matchRequest);
            triggerMatchmaking(playerIds.toString());
            logger.info("Match created successfully with players: {}", players);
        } catch (Exception e) {
            logger.error("Error creating match", e);
            throw new RuntimeException("Error creating match");
        }
    }

    public void triggerMatchmaking(String playerIds) {
        // Create message attributes if needed
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("AttributeKey", MessageAttributeValue.builder().stringValue("AttributeValue").dataType("String").build());

        // Define message body using proper JSON format
        Map<String, Object> messageBodyMap = new HashMap<>();
        messageBodyMap.put("playerIds", playerIds);

        try {
            // Convert map to JSON string using Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            String messageBody = objectMapper.writeValueAsString(messageBodyMap);

            // Send message to the matchmaking queue
            sqsService.sendMessageToQueue("admin", messageBody, messageAttributes);
            System.out.println("Automatically triggered matchmaking for players: " + playerIds);
        } catch (Exception e) {
            logger.error("Error creating JSON message body for matchmaking", e);
        }
    }

}
