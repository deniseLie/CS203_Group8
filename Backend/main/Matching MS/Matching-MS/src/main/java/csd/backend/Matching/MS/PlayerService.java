package csd.backend.Matching.MS;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.sqs.model.*;

@Service
public class PlayerService {
    private static final String PLAYERS_TABLE = "Players";
    private static final String MATCHES_TABLE = "Matches";
    private static final int MAX_PLAYERS = 8;

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

    // Update player's queue status in database
    public void deletePlayerStatus(Long playerId) {

        // Define the update to remove the 'queueStatus' attribute
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("queueStatus", AttributeValueUpdate.builder()
                .action("remove")
                .build());

        // Build the update request
        UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(Map.of("playerId", AttributeValue.builder().n(String.valueOf(playerId)).build()))
                .attributeUpdates(updates)
                .build();

        try {
            // Execute the update request
            dynamoDbClient.updateItem(updateRequest);
            System.out.println("Queue status for player " + playerId + " removed successfully.");
        } catch (Exception e) {
            System.err.println("Error removing player status: " + e.getMessage());
        }
    }
}
