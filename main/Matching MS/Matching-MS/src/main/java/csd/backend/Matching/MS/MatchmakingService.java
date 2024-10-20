package csd.backend.Matching.MS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MatchmakingService {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Autowired
    private SqsClient sqsClient;

    private static final String PLAYERS_TABLE = "Players";
    private static final String MATCHES_TABLE = "Matches";
    private static final String SQS_QUEUE_URL = "https://sqs.us-east-1.amazonaws.com/YOUR_ACCOUNT_ID/your-queue";
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

    // Check if there are enough players in the pool to start a match
    public List<Map<String, AttributeValue>> checkPlayersInQueue(int rankId) {
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

    // Create a new match with 8 players
    public void createMatch(List<Map<String, AttributeValue>> players) {
        Map<String, AttributeValue> matchItem = new HashMap<>();
        matchItem.put("matchId", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis())).build()); // matchId as timestamp

        // Add player emails to the match
        List<AttributeValue> playerList = players.stream()
                .map(player -> AttributeValue.builder().s(player.get("playerName").s()).build())
                .toList();

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

    // Method to handle SQS message processing
    public void processSqsMessages() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(SQS_QUEUE_URL)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(20)
                .build();

        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();

        for (Message message : messages) {
            String body = message.body();

            Map<String, Object> playerData = parseMessage(body);
            String email = (String) playerData.get("email");
            String playerName = (String) playerData.get("playerName");
            String queueStatus = (String) playerData.get("queueStatus");
            int rank = 1;

            // Add player to the matchmaking pool
            addPlayerToPool(playerName, email, queueStatus, rank);

            /// Check if there are enough players in the queue to create a match
            List<Map<String, AttributeValue>> players = checkPlayersInQueue(rank);
            if (players.size() >= MAX_PLAYERS) {
                createMatch(players);
                removePlayersFromQueue(players); 
                System.out.println("Match created with players: " + players);
            }

            // Delete the message from the queue after processing
            DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                    .queueUrl(SQS_QUEUE_URL)
                    .receiptHandle(message.receiptHandle())
                    .build();
            sqsClient.deleteMessage(deleteMessageRequest);
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
