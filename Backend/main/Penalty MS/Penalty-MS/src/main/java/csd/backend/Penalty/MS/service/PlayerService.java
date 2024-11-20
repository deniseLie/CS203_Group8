package csd.backend.Penalty.MS.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csd.backend.Penalty.MS.exception.PlayerNotFoundException;
import csd.backend.Penalty.MS.exception.PlayerUpdateException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@Service
public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);
    private static final String PLAYERS_TABLE = "Players";

    private final DynamoDbClient dynamoDbClient;

    @Autowired
    public PlayerService(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    // Add a player to the matchmaking pool (Create Player)
    public void addPlayerToPool(Long playerId, String queueStatus) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("playerId", createLongAttribute(playerId));
        item.put("queueStatus", createStringAttribute(queueStatus));
        item.put("banUntil", createLongAttribute(0L)); // Initially no ban
        item.put("banCount", createLongAttribute(0L)); // Initially no bans

        // Execute
        executePutItem(item, "Adding player to pool");
    }

    // Retrieve player details from the database (Read Player)
    public Map<String, AttributeValue> getPlayerDetails(Long playerId) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(Collections.singletonMap("playerId", createLongAttribute(playerId)))
                .build();

        // Execute
        try {
            GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);
            if (getItemResponse.item().isEmpty()) {
                throw new PlayerNotFoundException("Player with ID " + playerId + " not found.");
            }
            return getItemResponse.item();
        } catch (Exception e) {
            logger.error("Error retrieving player {}: {}", playerId, e.getMessage());
            throw new PlayerNotFoundException("Error retrieving player: " + playerId, e);
        }
    }

    // Retrieve the player's ban count
    public int getBanCount(Long playerId) {
        Map<String, AttributeValue> player = getPlayerDetails(playerId);

        // Error Handling
        if (player == null) throw new PlayerNotFoundException("Player with ID " + playerId + " not found.");

        // Get player detail
        return player.containsKey("banCount")
                ? Integer.parseInt(player.get("banCount").n())
                : 0;
    }

    // Get banned players
    public List<Map<String, AttributeValue>> getBannedPlayers() {

        // Scan DynamoDB for players with "banned" status and get all attributes
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName("Players")
                .filterExpression("queueStatus = :banned")
                .expressionAttributeValues(Map.of(":banned", createStringAttribute("banned")))
                .build();

        // Execute the scan request
        try {
            ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
            return scanResponse.items();
        } catch (Exception e) {
            logger.error("Error retrieving banned players: {}", e.getMessage());
            throw new RuntimeException("Error retrieving banned players", e);
        }
    }

    // Update player's status (Update Player Status)
    public void updatePlayerStatus(Long playerId, String queueStatus) {
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("queueStatus", AttributeValueUpdate.builder()
                .value(createStringAttribute(queueStatus))
                .action(AttributeAction.PUT)
                .build());

        // Execute
        executeUpdate(playerId, updates);
    }

    // Update the "banUntil" timestamp to ban a player (Update Player Ban)
    public void updateBanUntil(Long playerId, Long banUntil) {
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("banUntil", AttributeValueUpdate.builder()
                .value(createLongAttribute(banUntil))
                .action(AttributeAction.PUT)
                .build());

        // Execute
        executeUpdate(playerId, updates);
    }

    // Update the "banCount" timestamp to ban a player (Update Player Ban)
    public void updateBanCount(Long playerId, int banCount) {
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("banCount", AttributeValueUpdate.builder()
                .value(createLongAttribute((long) banCount))
                .action(AttributeAction.PUT)
                .build());

        // Execute
        executeUpdate(playerId, updates);   
    }

    // Delete player from the database (Delete Player)
    public void deletePlayer(Long playerId) {

        // Build the key to identify the item to be deleted
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("playerId", AttributeValue.builder().n(String.valueOf(playerId)).build());

        // Create the delete item request
        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(key)
                .build();

        // Execute the delete item request
        try {
            dynamoDbClient.deleteItem(deleteItemRequest);
            logger.info("Player with playerId {} deleted successfully.", playerId);
        } catch (DynamoDbException e) {
            logger.error("DynamoDB error deleting player {}: {}", playerId, e.getMessage());
            throw new RuntimeException("Error deleting player", e); 
        } catch (Exception e) {
            logger.error("Unexpected error deleting player {}: {}", playerId, e.getMessage());
            throw new RuntimeException("Unexpected error deleting player", e);
        }
        
    }

    // Helper method for executing update requests
    private void executeUpdate(Long playerId, Map<String, AttributeValueUpdate> updates) {
        try {
            UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                    .tableName(PLAYERS_TABLE)
                    .key(Collections.singletonMap("playerId", createLongAttribute(playerId)))
                    .attributeUpdates(updates)
                    .build();
            dynamoDbClient.updateItem(updateRequest);
            logger.info("Updated player {}: {}", playerId, updates);
        } catch (Exception e) {
            logger.error("Error updating player {}: {}", playerId, e.getMessage());
            throw new PlayerUpdateException("Error updating player", e);
        }
    }

    // Execute PutItemRequest
    private void executePutItem(Map<String, AttributeValue> item, String actionDescription) {
        try {
            PutItemRequest putItemRequest = PutItemRequest.builder()
                    .tableName(PLAYERS_TABLE)
                    .item(item)
                    .build();
            dynamoDbClient.putItem(putItemRequest);
            logger.info(actionDescription);
        } catch (Exception e) {
            logger.error("Error performing action: {}", actionDescription, e);
            throw new RuntimeException("Error performing action", e);
        }
    }

    // Helper method to create AttributeValue for a Long type
    private AttributeValue createLongAttribute(Long value) {
        return AttributeValue.builder().n(String.valueOf(value)).build();
    }

    // Helper method to create AttributeValue for a String type
    private AttributeValue createStringAttribute(String value) {
        return AttributeValue.builder().s(value).build();
    }
}
