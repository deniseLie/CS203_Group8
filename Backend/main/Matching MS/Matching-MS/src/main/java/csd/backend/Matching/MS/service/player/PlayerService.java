package csd.backend.Matching.MS.service.player;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import csd.backend.Matching.MS.exception.PlayerNotFoundException;
import csd.backend.Matching.MS.exception.PlayerUpdateException;
import csd.backend.Matching.MS.model.PlayerStatus;
import csd.backend.Matching.MS.utils.ResponseUtil;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

// QUEUE STATUS = available, queue, speedupQueue, ban (w/ banUntil)

@Service
public class PlayerService {

    private static final String PLAYERS_TABLE = "Players";
    private final DynamoDbClient dynamoDbClient;
    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    public PlayerService(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    // Helper method to execute update requests for player attributes
    private void updatePlayerAttribute(Long playerId, Map<String, AttributeValueUpdate> updates) {
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(Map.of("playerId", AttributeValue.builder().n(String.valueOf(playerId)).build())) 
                .attributeUpdates(updates)
                .build();

        try {

            // Execute the update request
            dynamoDbClient.updateItem(updateItemRequest);  
            logger.info("Successfully updated player {} in DynamoDB.", playerId);
        } catch (Exception e) {
            logger.error("Error updating player {} in DynamoDB", playerId, e);  
            throw new PlayerUpdateException(playerId);
        }
    }

    // Get player by playerId from the database
    public Map<String, AttributeValue> getPlayerById(Long playerId) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(Map.of("playerId", AttributeValue.builder().n(String.valueOf(playerId)).build())) // Retrieve by playerId
                .build();

        GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);
        Map<String, AttributeValue> player = getItemResponse.item();

        // Check player exist
        if (player == null) {
            throw new PlayerNotFoundException("Player with ID " + playerId + " was not found in the database.");
        }
        return player;
    }

    // Check player status and remaining ban time
    public Map<String, Object> checkPlayerStatus(Long playerId) {
        Map<String, Object> status = new HashMap<>();
        Map<String, AttributeValue> player = getPlayerById(playerId);  

        // Check if playerId exists in the response
        status.put("playerId", player.get("playerId") != null ? player.get("playerId").n() : null);
        status.put("queueStatus", player.get("queueStatus") != null ? player.get("queueStatus").s() : null);
        long remainingTime = 0;

        // Check if the player has a ban status
        if (player.get("banUntil") != null) {

            // Calculate remaining time until ban lifts
            long banUntil = Long.parseLong(player.get("banUntil").n());
            remainingTime = Math.max(0, banUntil - System.currentTimeMillis());  
        }
        status.put("remainingTime", remainingTime);  

        // Return the player status
        return status;  
    }

    // Check if the player is banned and return a ResponseEntity if so
    public ResponseEntity<Map<String, Object>> checkAndHandlePlayerBan(Long playerId) {
        Map<String, Object> playerStatus = checkPlayerStatus(playerId);  

        // Check if the player is banned and if the ban duration is still active
        if (playerStatus.containsKey("remainingTime") && (long) playerStatus.get("remainingTime") > 0) {
            long remainingTime = (long) playerStatus.get("remainingTime");
            Map<String, Object> response = new HashMap<>();

            // Return a forbidden response if banned
            return ResponseUtil.createForbiddenResponse("You are currently banned. Please try again in " + remainingTime / 1000 + " seconds.");
        }

        // Return a successful response if the player is not banned
        return ResponseUtil.createEmptyOkResponse();
    }

    // Retrieve players from the normal queue with the same rankId
    public List<Map<String, AttributeValue>> checkPlayersInQueue(Long rankId) {
        return queryPlayersByQueueAndRank(PlayerStatus.QUEUE.getStatus(), rankId, rankId);  
    }

    // Retrieve players from the speed-up queue with ranks near the specified rankId
    public List<Map<String, AttributeValue>> checkPlayersInSpeedUpQueue(Long rankId) {
        return queryPlayersByQueueAndRank(PlayerStatus.SPEEDUP_QUEUE.getStatus(), rankId - 1, rankId + 1); 
    }

    // Generic query for players in queue with rank filtering
    private List<Map<String, AttributeValue>> queryPlayersByQueueAndRank(String queueStatus, Long minRankId, Long maxRankId) {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(PLAYERS_TABLE)
                .filterExpression("queueStatus = :queueStatus and rankId BETWEEN :minRankId AND :maxRankId")
                .expressionAttributeValues(Map.of(
                        ":queueStatus", AttributeValue.builder().s(queueStatus).build(),
                        ":minRankId", AttributeValue.builder().n(String.valueOf(minRankId)).build(),
                        ":maxRankId", AttributeValue.builder().n(String.valueOf(maxRankId)).build()))
                .build();

        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);  // Execute the scan request
        return scanResponse.items();  // Return the list of players
    }

    // Get the rankId of the player
    public Long getPlayerRankId(Long playerId) {
        Map<String, AttributeValue> playerData = getPlayerById(playerId);

        // Extract rankId from the player's data (assuming rankId is stored as an integer)
        if (playerData != null && playerData.containsKey("rankId")) {
            return Long.parseLong(playerData.get("rankId").n());
            
        // Handle the case when rankId is not found
        } else {
            throw new PlayerNotFoundException(playerId);
        }
    }

    // Add a player to the matchmaking pool with their status and rank
    public void addPlayerToPool(Long playerId, String queueStatus, int rankId) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("playerId", AttributeValue.builder().n(String.valueOf(playerId)).build());
        item.put("queueStatus", AttributeValue.builder().s(queueStatus).build());
        item.put("rankId", AttributeValue.builder().n(String.valueOf(rankId)).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .item(item)
                .build();

        // Add the player to DynamoDB
        try {
            dynamoDbClient.putItem(putItemRequest);  
            logger.info("Successfully added player {} to the pool", playerId);
        } catch (Exception e) {
            logger.error("Error adding player {} to pool", playerId, e); 
            throw new PlayerUpdateException("Error adding player to pool for playerId: " + playerId);
        }
    }

    // Update player's rankId in the database
    public void updatePlayerRank(Long playerId, Long rankId) {
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("rankId", AttributeValueUpdate.builder().value(AttributeValue.builder().n(String.valueOf(rankId)).build()).action(AttributeAction.PUT).build());
        updatePlayerAttribute(playerId, updates);  // Update the player's rankId
    }

    // Update player's queue status in database
    public void updatePlayerStatus(Long playerId, String status) {
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("queueStatus", AttributeValueUpdate.builder().value(AttributeValue.builder().s(status).build()).action(AttributeAction.PUT).build());
        updatePlayerAttribute(playerId, updates);  // Update the player's queue status
    }

    // Update player's champion in the database
    public void updatePlayerChampion(Long playerId, String championId) {
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("championId", AttributeValueUpdate.builder().value(AttributeValue.builder().s(championId).build()).action(AttributeAction.PUT).build());
        updatePlayerAttribute(playerId, updates);  // Update the player's champion ID
    }

    // Remove player's championId from the database
    public void removePlayerChampion(Long playerId) {
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("championId", AttributeValueUpdate.builder().value(AttributeValue.builder().s("").build()).action(AttributeAction.PUT).build());
        updatePlayerAttribute(playerId, updates);  // Remove the championId (set it to empty)
    }

    // Update player's ban status and the time until they are banned
    public void updatePlayerBanStatus(Long playerId, String queueStatus, long banEndTime) {
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("queueStatus", AttributeValueUpdate.builder().value(AttributeValue.builder().s(queueStatus).build()).action(AttributeAction.PUT).build());
        updates.put("banUntil", AttributeValueUpdate.builder().value(AttributeValue.builder().n(String.valueOf(banEndTime)).build()).action(AttributeAction.PUT).build());
        updatePlayerAttribute(playerId, updates);  // Update the player's ban status and banUntil timestamp
    }

    // Delete player from the database
    public void deletePlayer(Long playerId) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("playerId", AttributeValue.builder().n(String.valueOf(playerId)).build());

        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(key)
                .build();

        // Delete player from DynamoDB
        try {
            dynamoDbClient.deleteItem(deleteItemRequest);  
            logger.info("Successfully deleted player {} from the database.", playerId);
        } catch (Exception e) {
            logger.error("Error deleting player {} from the database.", playerId, e); 
            throw new PlayerUpdateException(playerId);
        }
    }
}
