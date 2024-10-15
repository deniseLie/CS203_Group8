package csd.backend.Matching.MS;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;

import java.util.*;

public class MatchmakingService {

    private final DynamoDbClient dynamoDbClient;
    private final SqsClient sqsClient;

    private static final String PLAYERS_TABLE = "Players";
    private static final String MATCHES_TABLE = "Matches";

    // Constructor to inject DynamoDbClient and SqsClient
    public MatchmakingService(DynamoDbClient dynamoDbClient, SqsClient sqsClient) {
        this.dynamoDbClient = dynamoDbClient;
        this.sqsClient = sqsClient;
    }

    // Add a player to the matchmaking pool
    public void addPlayerToPool(String playerName, String email, String status, int rankId) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("player_name", AttributeValue.builder().s(playerName).build());
        item.put("email", AttributeValue.builder().s(email).build());
        item.put("status", AttributeValue.builder().s(status).build());
        item.put("rankId", AttributeValue.builder().n(String.valueOf(rankId)).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .item(item)
                .build();

        try {
            dynamoDbClient.putItem(putItemRequest);
        } catch (DynamoDbException e) {
            System.err.println("Unable to add player: " + playerName);
            e.printStackTrace();
        }
    }

    // Check if there are enough players in the pool to start a match
    public List<String> checkForMatches(int rankId, int maxPlayers) {
        List<String> matchedPlayers = new ArrayList<>();
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":queueStatus", AttributeValue.builder().s("queue").build());
        expressionAttributeValues.put(":rankId", AttributeValue.builder().n(String.valueOf(rankId)).build());

        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(PLAYERS_TABLE)
                .filterExpression("status = :queueStatus AND rankId = :rankId")
                .expressionAttributeValues(expressionAttributeValues)
                .limit(maxPlayers)
                .build();

        try {
            ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
            for (Map<String, AttributeValue> item : scanResponse.items()) {
                matchedPlayers.add(item.get("player_name").s());
            }
        } catch (DynamoDbException e) {
            System.err.println("Error scanning for players.");
            e.printStackTrace();
        }

        return matchedPlayers;
    }

    // Remove players from the pool after a match is found
    public void removePlayersFromQueue(List<String> playerNames) {
        for (String playerName : playerNames) {
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("player_name", AttributeValue.builder().s(playerName).build());

            Map<String, AttributeValueUpdate> updates = new HashMap<>();
            updates.put("status", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s("not queue").build())
                    .action(AttributeAction.PUT)
                    .build());

            UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                    .tableName(PLAYERS_TABLE)
                    .key(key)
                    .attributeUpdates(updates)
                    .build();

            try {
                dynamoDbClient.updateItem(updateRequest);
            } catch (DynamoDbException e) {
                System.err.println("Unable to remove players from queue.");
                e.printStackTrace();
            }
        }
    }

    // Create a new match with players
    public void createMatch(List<String> playerNames) {
        Map<String, AttributeValue> matchItem = new HashMap<>();
        matchItem.put("matchId", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis())).build());
        matchItem.put("players", AttributeValue.builder().l(
                playerNames.stream().map(name -> AttributeValue.builder().s(name).build()).toList()
        ).build());

        PutItemRequest matchRequest = PutItemRequest.builder()
                .tableName(MATCHES_TABLE)
                .item(matchItem)
                .build();

        try {
            dynamoDbClient.putItem(matchRequest);
        } catch (DynamoDbException e) {
            System.err.println("Unable to create match.");
            e.printStackTrace();
        }
    }

    // Delete a message from the SQS queue
    public void deleteMessageFromQueue(String receiptHandle, String queueUrl) {
        try {
            sqsClient.deleteMessage(DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(receiptHandle)
                    .build());
        } catch (Exception e) {
            System.err.println("Unable to delete message from SQS queue.");
            e.printStackTrace();
        }
    }
}
