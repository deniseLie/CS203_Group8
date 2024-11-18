package csd.backend.Matching.MS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import csd.backend.Matching.MS.Model.TournamentSize;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.sqs.model.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchmakingService {

    private final DynamoDbClient dynamoDbClient;
    private final SqsService sqsService;
    private final PlayerService playerService;

    public MatchmakingService(DynamoDbClient dynamoDbClient, SqsService sqsService, PlayerService playerService) {
        this.dynamoDbClient = dynamoDbClient;
        this.sqsService = sqsService;
        this.playerService = playerService;
    }

    private static final String PLAYERS_TABLE = "Players";
    private static final String MATCHES_TABLE = "Matches";
    private static final Logger logger = LoggerFactory.getLogger(MatchmakingService.class);

    // Add a player to the matchmaking pool
    public void addPlayerToPool(Long playerId, String queueStatus, int rankId) {

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("playerId", AttributeValue.builder().n(String.valueOf(playerId)).build());
        item.put("queueStatus", AttributeValue.builder().s(queueStatus).build());
        item.put("rankId", AttributeValue.builder().n(String.valueOf(rankId)).build());

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

    // Retrieve player details from the database
    private Map<String, AttributeValue> getPlayerDetails(Long playerId) {
        // Get player details
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(PLAYERS_TABLE)
                .key(Map.of("playerId", AttributeValue.builder().n(String.valueOf(playerId)).build()))
                .build();

        GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);
        return getItemResponse.item();
    }

    // Check Player Status: If banned, return remaining ban time
    public Map<String, Object> checkPlayerStatus(Long playerId) {
        Map<String, AttributeValue> player = getPlayerDetails(playerId);
        Map<String, Object> status = new HashMap<>();
    
        if (player != null) {
            AttributeValue playerIdAttr = player.get("playerId");
            AttributeValue queueStatusAttr = player.get("queueStatus");
            AttributeValue banUntilAttr = player.get("banUntil");
    
            status.put("playerId", playerIdAttr != null ? playerIdAttr.n() : null);
            status.put("queueStatus", queueStatusAttr != null ? queueStatusAttr.s() : null);
    
            if (banUntilAttr != null && banUntilAttr.n() != null) {
                long banUntil = Long.parseLong(banUntilAttr.n());
                long remainingTime = banUntil > System.currentTimeMillis() ? banUntil - System.currentTimeMillis() : 0;
                status.put("remainingTime", Long.valueOf(remainingTime));
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

        int maxPlayers = TournamentSize.getTournamentSize();

        // Get players queueing + same rankId
        List<Map<String, AttributeValue>> players = checkPlayersInQueue(rankId);

        // check do we have enough players
        if (players.size() >= maxPlayers) {

            // Take the first MAX_PLAYERS players and create a match
            List<Map<String, AttributeValue>> playersToMatch = players.subList(0, maxPlayers);
            createTournament(playersToMatch);
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
            AttributeValue playerIdAttr = player.get("playerId");
            if (playerIdAttr != null && playerIdAttr.s() != null) {
                String playerId = playerIdAttr.s();
    
                Map<String, AttributeValueUpdate> updates = new HashMap<>();
                updates.put("queueStatus", AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().s("not queue").build())
                        .action(AttributeAction.PUT)
                        .build());
    
                UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                        .tableName(PLAYERS_TABLE)
                        .key(Map.of("playerId", AttributeValue.builder().s(playerId).build()))
                        .attributeUpdates(updates)
                        .build();
    
                dynamoDbClient.updateItem(updateRequest);
            } else {
                logger.warn("Missing playerId for a player in the queue.");
            }
        }
    }
    

    // Update Player Status - ban
    public void updatePlayerBanStatus(Long playerId, String queueStatus, long banEndTime) {
        
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
                .key(Map.of("playerId", AttributeValue.builder().s(String.valueOf(playerId)).build())) // Player key
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
    public void createTournament(List<Map<String, AttributeValue>> players) {
        Map<String, AttributeValue> matchItem = new HashMap<>();
        matchItem.put("matchId", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis())).build());
    
        List<AttributeValue> playerList = new ArrayList<>();
        List<String> playerIdsAndChampions = new ArrayList<>(); // List to accumulate playerId and championId pairs

        for (Map<String, AttributeValue> player : players) {
            Long playerId = Long.parseLong(player.get("playerId").n());
            String championId = player.get("championId").s();  // Assuming championId is a string
            
            // Add player to the match player list
            playerList.add(AttributeValue.builder().n(String.valueOf(playerId)).build());
            
            // Add playerId and championId to the list (e.g., "123,ChampionA")
            playerIdsAndChampions.add(playerId + "," + championId);
            
            // Unqueue player
            playerService.updatePlayerStatus(playerId, "unqueue");
        }
    
        matchItem.put("playerIds", AttributeValue.builder().l(playerList).build());
    
        PutItemRequest matchRequest = PutItemRequest.builder()
                .tableName(MATCHES_TABLE)
                .item(matchItem)
                .build();

        try {
            // Store the match in DynamoDB
            dynamoDbClient.putItem(matchRequest);
            
            // Pass the playerIds and their championIds to matchmaking
            triggerMatchmaking(playerIdsAndChampions, LocalDateTime.now());
            logger.info("Match created successfully with players: {}", players);
        } catch (Exception e) {
            logger.error("Error creating match", e);
            throw new RuntimeException("Error creating match");
        }
    }
    

    // Method to send out matchmaking sqs to other services
    public void triggerMatchmaking(List<String> playerIdsAndChampions, LocalDateTime tournamentStartTime) {

        // Prepare message attributes
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("actionType", MessageAttributeValue.builder()
                .stringValue("createTournament")
                .dataType("String")
                .build());

        // Prepare the message body using a Map
        Map<String, Object> messageBodyMap = new HashMap<>();
        messageBodyMap.put("timestampStart", tournamentStartTime.toString());  // Use toString to convert LocalDateTime
        messageBodyMap.put("tournamentSize", playerIdsAndChampions.size());
        messageBodyMap.put("players", playerIdsAndChampions);

        try {
            // Convert map to JSON string using Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            String messageBody = objectMapper.writeValueAsString(messageBodyMap);

            // Send message to the matchmaking queue
            sqsService.sendMessageToQueue("admin", messageBody, messageAttributes);
            logger.info("Automatically triggered matchmaking for players: {}", playerIdsAndChampions);
        } catch (Exception e) {
            logger.error("Error creating JSON message body for matchmaking", e);
        }
    }


}
