package csd.backend.Penalty.MS.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.*;

@Service
public class PenaltyService {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    private final SqsService sqsService;

    @Autowired
    public PenaltyService(SqsService sqsService) {
        this.sqsService = sqsService;
    }

    private static final String PLAYERS_TABLE = "Players";
    private static final Logger logger = LoggerFactory.getLogger(PenaltyService.class);

    // Constant for base ban duration in seconds
    private static final int BASE_BAN_DURATION_IN_SECONDS = 300; // 5 minutes

    // Add a player to the matchmaking pool
    public void addPlayerToPool(long playerId, String queueStatus) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("playerId", AttributeValue.builder().n(String.valueOf(playerId)).build());
        item.put("queueStatus", AttributeValue.builder().s(queueStatus).build());
        item.put("banUntil", AttributeValue.builder().n("0").build());
        item.put("banCount", AttributeValue.builder().n("0").build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .item(item)
                .build();

        try {
            logger.info("Attempting to add player {} with item: {}", playerId, item);
            dynamoDbClient.putItem(putItemRequest);
            logger.info("Successfully added player {} to the pool", playerId);
        } catch (Exception e) {
            logger.error("Error adding player {} to pool", playerId, e);
            throw new RuntimeException("Error adding player to pool");
        }
    }

    // Ban player for a specified duration based on their ban count
    public void banPlayer(String playerId) {

        // Retrieve the player's current ban count
        Map<String, AttributeValue> player = getPlayerDetails(playerId);
        int banCount = player != null && player.containsKey("banCount")
                ? Integer.parseInt(player.get("banCount").n())
                : 0;

        // Adjust the ban duration based on the ban count
        // Add 60 seconds for each previous ban
        int dynamicDuration = BASE_BAN_DURATION_IN_SECONDS + (banCount * 60); 

        // Calculate ban end time - convert seconds to milliseconds
        long banEndTime = System.currentTimeMillis() + (dynamicDuration * 1000); 

        Map<String, AttributeValueUpdate> updates = new HashMap<>();

        // Update queue status to "banned"
        updates.put("queueStatus", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s("banned").build())
                .action(AttributeAction.PUT)
                .build());

        // Update ban until time
        updates.put("banUntil", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().n(String.valueOf(banEndTime)).build())
                .action(AttributeAction.PUT)
                .build());

        // Update ban count - Increment the ban count
        updates.put("banCount", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().n(String.valueOf(banCount + 1)).build())
                .action(AttributeAction.PUT)
                .build());

        // Prepare and execute the update request
        UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(Map.of("playerId", AttributeValue.builder().n(playerId).build()))
                .attributeUpdates(updates)
                .build();

        // Update in the database
        dynamoDbClient.updateItem(updateRequest);
        logger.info("Player {} banned for {} seconds (dynamic duration)", playerId, dynamicDuration);

        // Prepare the message body to send to the matchmaking queue - SQS
        String messageBody = playerId + ", until: " + new Date(banEndTime);

        // Prepare message attributes (optional)
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("actionType", MessageAttributeValue.builder()
                .stringValue("ban")
                .dataType("String")
                .build());

        // Send the message to the matchmaking queue
        sqsService.sendMessageToQueue("matchmaking", messageBody, messageAttributes);
        logger.info("Message sent to matchmaking queue: {}", messageBody);
    }

    // Check player status and return remaining ban time if banned
    public Map<String, Object> checkPlayerStatus(String playerId) {

        // Get Player details from database
        Map<String, AttributeValue> player = getPlayerDetails(playerId);
        Map<String, Object> status = new HashMap<>();

        if (player != null && player.containsKey("playerId") && player.containsKey("queueStatus") && player.containsKey("banUntil")) {
            // Safely retrieve fields from player map
            status.put("playerId", player.get("playerId").s());
            status.put("queueStatus", player.get("queueStatus").n());
            long banUntil = Long.parseLong(player.get("banUntil").n());
    
            // Check if the player is still banned
            if (banUntil > System.currentTimeMillis()) {
                // Calculate remaining ban time
                long remainingTime = banUntil - System.currentTimeMillis();
                status.put("remainingTime", remainingTime);
            } else {
                status.put("remainingTime", 0); // Not banned
            }
        } else {
            // Log a warning and return an error if the player data is incomplete or missing
            logger.warn("Player {} not found or missing required fields", playerId);
            status.put("error", "Player not found");
        }
        return status;
    }

    // Scheduled task to unban players whose ban duration has expired
    @Scheduled(fixedRate = 60000) // Check every minute
    public void unbanExpiredPlayers() {

        // Request for players with "banned" status
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(PLAYERS_TABLE)
                .filterExpression("queueStatus = :banned")
                .expressionAttributeValues(Map.of(":banned", AttributeValue.builder().s("banned").build()))
                .build();

        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);

        // Loop through players
        for (Map<String, AttributeValue> player : scanResponse.items()) {

            // Get the player name and ban until
            String playerId = player.get("playerId").s();
            long banUntil = Long.parseLong(player.get("banUntil").n());

            // Check if the ban has expired
            if (banUntil <= System.currentTimeMillis()) {
                updatePlayerStatus(playerId, "available"); // Change status to available
                logger.info("Player {} has been unbanned and is now available", playerId);
            }
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

    // Update player's status
    public void updatePlayerStatus(String playerId, String queueStatus) {
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("queueStatus", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(queueStatus).build())
                .action(AttributeAction.PUT)
                .build());

        UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(Map.of("playerId", AttributeValue.builder().s(playerId).build()))
                .attributeUpdates(updates)
                .build();

        dynamoDbClient.updateItem(updateRequest);
        logger.info("Updated player {} status to {}", playerId, queueStatus);
    }

    // Send a message to the Penalty Queue
    public void sendMessageToPenaltyQueue(String messageBody, Map<String, MessageAttributeValue> messageAttributes) {
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(sqsService.getQueueUrl("penalty"))
                .messageBody(messageBody)
                .messageAttributes(messageAttributes)
                .messageGroupId("PenaltyServiceGroup") // Required for FIFO queues
                .messageDeduplicationId(String.valueOf(messageBody.hashCode())) // Ensures unique messages in FIFO
                .build();

        SendMessageResponse response = sqsService.getSqsClient().sendMessage(sendMsgRequest);
        System.out.println("Message sent to Penalty Queue with MessageId: " + response.messageId());
    }

    // Delete player
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
}
