package csd.backend.Matching.MS;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MatchmakingQueueListener {

    private final SqsService sqsService;
    private final MatchmakingService matchmakingService;
    private final PlayerService playerService;

    @Autowired
    public MatchmakingQueueListener(SqsService sqsService, MatchmakingService matchmakingService, PlayerService playerService) {
        this.sqsService = sqsService;
        this.matchmakingService = matchmakingService;
        this.playerService = playerService;
    }

    // Listen for messages in the Matching Queue
    public void listenToMatchingQueue() {
        String queueUrl = sqsService.getQueueUrl("matchmaking");

        while (true) {
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(10)  // Process up to 10 messages at once
                    .waitTimeSeconds(20)      // Long polling for efficiency
                    .build();

            ReceiveMessageResponse response = sqsService.getSqsClient().receiveMessage(receiveMessageRequest);

            for (Message message : response.messages()) {
                System.out.println("Processing Queue message: " + message.body());

                // Extract the message attributes from the received message
                Map<String, MessageAttributeValue> messageAttributes = message.messageAttributes();

                // Pass both the message body and message attributes to the processing method
                processMessage(message.body(), messageAttributes);

                // Delete message after processing
                deleteMessageFromQueue(queueUrl, message);
            }
        }
    }

    // Placeholder for message processing logic
    private void processMessage(String messageBody, Map<String, MessageAttributeValue> messageAttributes) {

        System.out.println("Message processed: " + messageBody);

        // Check if the message has the 'actionType' attribute
        String actionType = getActionTypeFromMessageAttributes(messageAttributes);
        
        // Skip processing if actionType is not found
        if (actionType == null) {
            System.err.println("No actionType found in the message attributes. Skipping message.");
            return;  
        }
        
        // Based on the actionType
        switch (actionType) {
            case "addPlayer":
                processAddPlayer(messageBody);
                break;
            case "ban": 
                processBanPlayer(messageBody);
                break;       
            case "updatePlayerProfile":
                processUpdatePlayer(messageBody);
                break;
            default: 
                System.err.println("Unknown action type: " + actionType);
                break;

        }
    }

    // Helper method to get the 'actionType' from the message attributes
    private String getActionTypeFromMessageAttributes(Map<String, MessageAttributeValue> messageAttributes) {
        if (messageAttributes != null && messageAttributes.containsKey("actionType")) {
            return messageAttributes.get("actionType").stringValue();
        }
        return null; 
    }

    // Process Add Player to DB
    private void processAddPlayer (String messageBody) {
        // Extract player details
        Map<String, String> playerData = parsePlayerDataFromMessage(messageBody);

        // ADD PLAYER
        if (playerData != null) {
            // Call addPlayerToPool with extracted data
            String playerId = playerData.get("playerId");
            String email = playerData.get("email");
            String queueStatus = playerData.getOrDefault("queueStatus", "available");
            int rankId = Integer.parseInt(playerData.getOrDefault("rankId", "1")); 

            matchmakingService.addPlayerToPool(playerId, email, queueStatus, rankId);
            System.out.println("Player added to pool: " + playerId);
        }
    }

    // Ban Player
    private void processBanPlayer(String messageBody) {

        // Only process the message if it's a ban-related action
        String[] parts = messageBody.split(", until: ");
        if (parts.length == 2) {
            String playerId = parts[0];         // Player ID
            String banUntilString = parts[1].trim();  // Ban end time (until when)

            // Log for debugging
            System.out.println("Player ID: " + playerId + ", Ban Until: " + banUntilString);
            try {

                // Parse the banUntilString to Date object
                Date banUntil = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(banUntilString);
                long banEndTime = banUntil.getTime();
                
                // Use the matchmakingService to update the player's ban status in the database
                matchmakingService.updatePlayerBanStatus(playerId, "banned", banEndTime);

            } catch (Exception e) {
                System.err.println("Failed to process ban end time: " + e.getMessage());
            }
        } else {
            System.err.println("Message format invalid: " + messageBody);
        }
    }

    // Parse player data from JSON message body
    private Map<String, String> parsePlayerDataFromMessage(String messageBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> playerData = new HashMap<>();

        try {
            // Parse JSON string into JsonNode
            JsonNode rootNode = objectMapper.readTree(messageBody);

            // Extract fields from JSON
            String playerId = rootNode.path("playerId").asText();
            String email = rootNode.path("email").asText(null);
            String queueStatus = rootNode.path("queueStatus").asText("available");

            // Populate the map with extracted values
            playerData.put("playerId", playerId);
            playerData.put("email", email);
            playerData.put("queueStatus", queueStatus);

            return playerData;
        } catch (Exception e) {
            System.out.println("Failed to parse player data from message: " + e.getMessage());
            return null;
        }
    }

    // Process Update Player action 
    private void processUpdatePlayer(String messageBody) {
        try {
            // Parse the update player profile request
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(messageBody);

            Long playerId = rootNode.path("playerId").asLong();
            Long rankId = rootNode.path("rank").asLong();

            playerService.updatePlayerRank(playerId, rankId);
            System.out.println("Player profile updated for playerId: " + playerId);
        } catch (Exception e) {
            System.err.println("Failed to process update player message: " + e.getMessage());
        }
    }

    // Helper method to delete the message from the queue after processing
    private void deleteMessageFromQueue(String queueUrl, Message message) {
        DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(message.receiptHandle())
                .build();
        sqsService.getSqsClient().deleteMessage(deleteRequest);
        System.out.println("Queue message processed and deleted.");
    }
}