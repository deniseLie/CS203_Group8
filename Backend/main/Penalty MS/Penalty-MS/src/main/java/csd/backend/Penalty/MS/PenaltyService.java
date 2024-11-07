package csd.backend.Penalty.MS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.sqs.SqsClient; 
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@Service
public class PenaltyService {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Autowired
    private SqsClient sqsClient;

    private static final String PLAYERS_TABLE = "Players";
    private static final String SQS_QUEUE_URL = "https://sqs.ap-southeast-1.amazonaws.com/your_account_id/your_queue_name"; // Update with actual SQS URL
    private static final Logger logger = LoggerFactory.getLogger(PenaltyService.class);

    // Constant for base ban duration in seconds
    private static final int BASE_BAN_DURATION_IN_SECONDS = 300; // 5 minutes

    // Add a player to the matchmaking pool
    public void addPlayerToPool(String playerName, String email, String queueStatus) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("playerName", AttributeValue.builder().s(playerName).build());
        item.put("email", AttributeValue.builder().s(email).build());
        item.put("queueStatus", AttributeValue.builder().s(queueStatus).build());
        item.put("banUntil", AttributeValue.builder().n("0").build());
        item.put("banCount", AttributeValue.builder().n("0").build());

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

    // Ban player for a specified duration based on their ban count
    public void banPlayer(String playerName) {

        // Retrieve the player's current ban count
        Map<String, AttributeValue> player = getPlayerDetails(playerName);
        int banCount = player != null && player.containsKey("banCount")
                ? Integer.parseInt(player.get("banCount").n())
                : 0;

        // Adjust the ban duration based on the ban count
        // Add 60 seconds for each previous ban
        int dynamicDuration = BASE_BAN_DURATION_IN_SECONDS + (banCount * 60); 

        // Calculate ban end time
        // Convert seconds to milliseconds
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
                .key(Map.of("playerName", AttributeValue.builder().s(playerName).build()))
                .attributeUpdates(updates)
                .build();

        // Update in the database
        dynamoDbClient.updateItem(updateRequest);
        logger.info("Player {} banned for {} seconds (dynamic duration)", playerName, dynamicDuration);
    }

    // Check player status and return remaining ban time if banned
    public Map<String, Object> checkPlayerStatus(String playerName) {

        // Get Players
        Map<String, AttributeValue> player = getPlayerDetails(playerName);
        Map<String, Object> status = new HashMap<>();

        if (player != null) {
            status.put("playerName", player.get("playerName").s());
            status.put("queueStatus", player.get("queueStatus").s());
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
            logger.warn("Player {} not found", playerName);
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
            String playerName = player.get("playerName").s();
            long banUntil = Long.parseLong(player.get("banUntil").n());

            // Check if the ban has expired
            if (banUntil <= System.currentTimeMillis()) {
                updatePlayerStatus(playerName, "available"); // Change status to available
                logger.info("Player {} has been unbanned and is now available", playerName);
            }
        }
    }

    // Retrieve player details from the database
    private Map<String, AttributeValue> getPlayerDetails(String playerName) {
        // Get player details
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(Map.of("playerName", AttributeValue.builder().s(playerName).build()))
                .build();

        GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);
        return getItemResponse.item();
    }

    // Update player's status
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
        logger.info("Updated player {} status to {}", playerName, queueStatus);
    }
    
    // Method to handle SQS message processing
    public void processSqsMessages() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(SQS_QUEUE_URL)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(20)
                .build();

        // Receive messages from the SQS queue
        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();

        for (Message message : messages) {
            String body = message.body();

            // Parse the SQS message body and get player data
            Map<String, Object> playerData = parseMessage(body);
            String email = (String) playerData.get("email");
            String playerName = (String) playerData.get("playerName");
            String queueStatus = (String) playerData.get("queueStatus");

            // Add player to the matchmaking pool and handle any exceptions
            try {
                addPlayerToPool(playerName, email, queueStatus);
                logger.info("Successfully added player {} to the pool from SQS message.", playerName);
            } catch (Exception e) {
                logger.error("Error adding player {} to the pool from SQS message: {}", playerName, e.getMessage());
                continue; // Skip this message and proceed to the next
            }

            // Delete the message from the queue after processing
            DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                    .queueUrl(SQS_QUEUE_URL)
                    .receiptHandle(message.receiptHandle())
                    .build();

            try {
                sqsClient.deleteMessage(deleteMessageRequest);
                logger.info("Deleted message for player {} from the SQS queue.", playerName);
            } catch (Exception e) {
                logger.error("Failed to delete message for player {}: {}", playerName, e.getMessage());
            }
        }
    }

    private Map<String, Object> parseMessage(String body) {
        // Parse the SQS message body and return player data as a map
        // You can replace this with JSON deserialization in a real-world case
        Map<String, Object> playerData = new HashMap<>();
        playerData.put("email", "player1@example.com"); // Replace with actual parsing
        playerData.put("playerName", "Player1"); // Replace with actual parsing
        playerData.put("queueStatus", "queue"); // Replace with actual parsing
        return playerData;
    }
}
