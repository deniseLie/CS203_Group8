package csd.backend.Matching.MS.service.player;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Service
public class PlayerService {

    private static final String PLAYERS_TABLE = "Players";
    private final DynamoDbClient dynamoDbClient;

    @Autowired
    public PlayerService(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    // Get Player by playerId in the database
    public Map<String, AttributeValue> getPlayerById(Long playerId) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(Map.of("playerId", AttributeValue.builder().n(String.valueOf(playerId)).build()))
                .build();

        GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);
        return getItemResponse.item();
    }

    // Get the rankId of the player
    public Long getPlayerRankId(Long playerId) {
        Map<String, AttributeValue> playerData = getPlayerById(playerId);

        // Extract rankId from the player's data (assuming rankId is stored as an integer)
        if (playerData != null && playerData.containsKey("rankId")) {
            AttributeValue rankIdAttribute = playerData.get("rankId");
            return Long.parseLong(rankIdAttribute.n());  // Convert from string to integer
        } else {
            // Handle the case when rankId is not found
            throw new RuntimeException("Player rankId not found for playerId: " + playerId);
        }
    }

    // Update player's rankId in the database
    public void updatePlayerRank(Long playerId, Long rankId) {
        // Assuming rankId is part of the player's data in DynamoDB, update the rankId here
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("rankId", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().n(String.valueOf(rankId)).build())
                .action(AttributeAction.PUT)
                .build());

        // Update the player's rankId in DynamoDB
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(Map.of("playerId", AttributeValue.builder().n(String.valueOf(playerId)).build()))
                .attributeUpdates(updates)
                .build();

        dynamoDbClient.updateItem(updateItemRequest);
    }

    // Update player's queue status in database
    public void updatePlayerStatus(Long playerId, String status) {
        Map<String, AttributeValueUpdate> updates = new HashMap<>();

        updates.put("status", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(status).build())
                .action(AttributeAction.PUT)
                .build());

        UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(Map.of("playerId", AttributeValue.builder().n(String.valueOf(playerId)).build()))
                .attributeUpdates(updates)
                .build();

        dynamoDbClient.updateItem(updateRequest);
    }

    // Delete player from the database
    public void deletePlayer(Long playerId) {
        // Build the key to identify the item to be deleted
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("playerId", AttributeValue.builder().n(String.valueOf(playerId)).build());

        // Create the delete item request
        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(key)
                .build();

        try {
            // Execute the delete item request
            dynamoDbClient.deleteItem(deleteItemRequest);
            System.out.println("Player with playerId " + playerId + " deleted successfully.");
        } catch (Exception e) {
            System.err.println("Error deleting player: " + e.getMessage());
        }
    }

    
    // Update player's champion in the database
    public void updatePlayerChampion(Long playerId, String championId) {
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("championId", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(championId).build())  // Store championId as a string
                .action(AttributeAction.PUT)  // Use PUT action to update the attribute
                .build());

        // Update the player's championId in DynamoDB
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(Map.of("playerId", AttributeValue.builder().n(String.valueOf(playerId)).build())) // Use playerId as key
                .attributeUpdates(updates)  // Specify the updates to be made
                .build();

        try {
            dynamoDbClient.updateItem(updateItemRequest);  // Execute the update request
        } catch (Exception e) {
        }
    }

    // Remove player's championId from the database
    public void removePlayerChampion(Long playerId) {
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("championId", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s("").build())  // Set championId to an empty string or null equivalent
                .action(AttributeAction.PUT)  // Use PUT action to update the attribute
                .build());

        // Update the player's championId in DynamoDB (set it to empty string or null)
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(Map.of("playerId", AttributeValue.builder().n(String.valueOf(playerId).toString()).build())) // Use playerId as key
                .attributeUpdates(updates)  // Specify the updates to be made
                .build();

        try {
            dynamoDbClient.updateItem(updateItemRequest);  // Execute the update request
        } catch (Exception e) {
        }
    }


}
