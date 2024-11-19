package csd.backend.Matching.MS.service.tournament;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import csd.backend.Matching.MS.service.sqs.SqsService;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.sqs.model.*;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TournamentService {

    private static final Logger logger = LoggerFactory.getLogger(TournamentService.class);

    private final DynamoDbClient dynamoDbClient;
    private final SqsService sqsService;

    // Constants for DynamoDB table names
    private static final String MATCHES_TABLE = "Matches";

    // Constructor to inject required services
    public TournamentService(DynamoDbClient dynamoDbClient, SqsService sqsService) {
        this.dynamoDbClient = dynamoDbClient;
        this.sqsService = sqsService;
    }

    // Creates a new match with the players that were selected.
    public Long createTournament(List<Map<String, AttributeValue>> players) {
        Map<String, AttributeValue> matchItem = prepareMatchItem(players);
        saveMatchToDatabase(matchItem);
        triggerMatchmaking(matchItem);

        // Extract matchId and return it as a String
        return Long.parseLong(matchItem.get("matchId").n()); 
    }
    
    // Prepare match item data and generate a sequential match ID
    private Map<String, AttributeValue> prepareMatchItem(List<Map<String, AttributeValue>> players) {
        Map<String, AttributeValue> matchItem = new HashMap<>();
        
        // Generate a unique match ID using the current timestamp (milliseconds)
        long matchId = System.currentTimeMillis();  // Use current time in milliseconds as match ID
        matchItem.put("matchId", AttributeValue.builder().n(String.valueOf(matchId)).build());
        
        // Prepare player details
        List<AttributeValue> playerList = preparePlayerList(players);
        matchItem.put("players", AttributeValue.builder().l(playerList).build());

        // Get the tournament start time and format it to string
        LocalDateTime tournamentStart = LocalDateTime.now();
        String formattedStartTime = tournamentStart.toString();

        // Add tournament start time
        matchItem.put("tournamentStart", AttributeValue.builder().s(formattedStartTime).build());

        return matchItem;
    }
    
    // Prepare the list of players in the required format
    private List<AttributeValue> preparePlayerList(List<Map<String, AttributeValue>> players) {
        List<AttributeValue> playerList = new ArrayList<>();

        // Loop through players
        for (Map<String, AttributeValue> player : players) {
            Long playerId = Long.parseLong(player.get("playerId").n());
            Long championId = Long.parseLong(player.get("playerId").n());

            // Creating player data in the format { playerId: playerId, championId: championId }
            Map<String, AttributeValue> playerData = new HashMap<>();
            playerData.put("playerId", AttributeValue.builder().n(String.valueOf(playerId)).build());
            playerData.put("championId", AttributeValue.builder().n(String.valueOf(championId)).build());

            playerList.add(AttributeValue.builder().m(playerData).build());
        }

        return playerList;
    }
    
    // Save Match Detail to Database
    private void saveMatchToDatabase(Map<String, AttributeValue> matchItem) {
        PutItemRequest matchRequest = PutItemRequest.builder()
                .tableName(MATCHES_TABLE)
                .item(matchItem)
                .build();
        try {
            dynamoDbClient.putItem(matchRequest);
        } catch (Exception e) {
            logger.error("Error saving match", e);
            throw new RuntimeException("Error saving match");
        }
    }

    // Sends a message to trigger matchmaking with player details
    public void triggerMatchmaking(Map<String, AttributeValue> matchItem) {
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("actionType", MessageAttributeValue.builder()
                .stringValue("createTournament")
                .dataType("String")
                .build());

        // Extract players' data from matchItem
        List<Map<String, String>> playerList = extractPlayerDetails(matchItem);

        // Building the message body with the required tournament data
        Map<String, Object> messageBodyMap = new HashMap<>();
        messageBodyMap.put("matchId", matchItem.get("matchId").n());
        messageBodyMap.put("timestampStart", matchItem.get("tournamentStart").s());
        messageBodyMap.put("tournamentSize", playerList.size());
        messageBodyMap.put("players", playerList);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String messageBody = objectMapper.writeValueAsString(messageBodyMap);

            sqsService.sendMessageToQueue("admin", messageBody, messageAttributes);
            logger.info("Matchmaking triggered for players: {}", playerList);
        } catch (Exception e) {
            logger.error("Error creating JSON message body for matchmaking", e);
        }
    }

    // Extract players' details from the match item
    private List<Map<String, String>> extractPlayerDetails(Map<String, AttributeValue> matchItem) {
        List<Map<String, String>> playerList = new ArrayList<>();
        
        // Loop through players in the matchItem and extract details
        for (AttributeValue player : matchItem.get("players").l()) {
            Map<String, String> playerData = new HashMap<>();
            Map<String, AttributeValue> playerMap = player.m();

            playerData.put("playerId", playerMap.get("playerId").n());
            playerData.put("championId", playerMap.get("championId").n());
            playerList.add(playerData);
        }
        
        return playerList;
    }
}
