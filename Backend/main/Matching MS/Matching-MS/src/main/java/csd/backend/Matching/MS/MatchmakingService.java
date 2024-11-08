package csd.backend.Matching.MS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.*;

@Service
public class MatchmakingService {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    private final SqsService sqsService;

    @Autowired
    public MatchmakingService(SqsService sqsService) {
        this.sqsService = sqsService;
    }

    private static final String PLAYERS_TABLE = "Players";
    private static final String MATCHES_TABLE = "Matches";
    private static final int MAX_PLAYERS = 8;
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

    // Check Match : Enough Players with same rankId
    public boolean checkForMatch(int rankId) {

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
    private List<Map<String, AttributeValue>> checkPlayersInQueue(int rankId) {
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
    public List<Map<String, AttributeValue>> checkPlayersInSpeedUpQueue(int rankId) {
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

    // Update player's queue status in database
    public void updatePlayerStatus(String playerName, String queueStatus) {
        Map<String, AttributeValueUpdate> updates = new HashMap<>();

        updates.put("queueStatus", AttributeValueUpdate.builder()
            .value(AttributeValue.builder().s(queueStatus).build())
            .action(AttributeAction.PUT)
            .build());

        UpdateItemRequest updateRequest = UpdateItemRequest.builder()
            .tableName(PLAYERS_TABLE)
            .key(Map.of("playerName", AttributeValue.builder().s(playerName).build()))
            .attributeUpdates(updates)
            .build();
        
        dynamoDbClient.updateItem(updateRequest);
        logger.info("Updated player status to '{}' for player: {}", queueStatus, playerName);
    }

    // Create a new match with players from the database
    public void createMatch(List<Map<String, AttributeValue>> players) {
        Map<String, AttributeValue> matchItem = new HashMap<>();
        matchItem.put("matchId", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis())).build()); // Use timestamp as matchId
    
        // Add player names to the match and update their queue status
        List<AttributeValue> playerList = new ArrayList<>();
        for (Map<String, AttributeValue> player : players) {
            String playerName = player.get("playerName").s();
            playerList.add(AttributeValue.builder().s(playerName).build());
            updatePlayerStatus(playerName, "unqueue");  
        }
    
        matchItem.put("players", AttributeValue.builder().l(playerList).build());
    
        PutItemRequest matchRequest = PutItemRequest.builder()
                .tableName(MATCHES_TABLE)
                .item(matchItem)
                .build();
    
        try {
            dynamoDbClient.putItem(matchRequest);
            logger.info("Match created successfully with players: {}", players);
        } catch (Exception e) {
            logger.error("Error creating match", e);
            throw new RuntimeException("Error creating match");
        }
    }

    public void triggerMatchmaking(String playerId) {
        // Create message attributes if needed
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("AttributeKey", MessageAttributeValue.builder().stringValue("AttributeValue").dataType("String").build());

        // Define message body
        String messageBody = "{\"action\": \"match_players\", \"player_id\": \"" + playerId + "\"}";

        // Send message to the matchmaking queue
        sqsService.sendMessageToQueue("matchmaking", messageBody, messageAttributes);
        System.out.println("Automatically triggered matchmaking for player: " + playerId);
    }
}
