package csd.backend.Admin;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import csd.backend.Admin.Model.Tournament.*;
import csd.backend.Admin.Model.User.*;
import csd.backend.Admin.Service.*;
import csd.backend.Admin.Service.User.PlayerService;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AdminQueueListener {

    private final SqsService sqsService;
    private final UserService userService;
    private final PlayerService playerService;
    private final TournamentService tournamentService;
    private final TournamentPlayerService tournamentPlayerService;

    @Autowired
    public AdminQueueListener(
        SqsService sqsService, UserService userService, PlayerService playerService,
        TournamentService tournamentService, TournamentPlayerService tournamentPlayerService
    ) {
        this.sqsService = sqsService;
        this.userService = userService;
        this.playerService = playerService;
        this.tournamentService = tournamentService;
        this.tournamentPlayerService = tournamentPlayerService;
    }

    // Listen for messages in the Admin Queue
    public void listenToAdminQueue() {
        String queueUrl = sqsService.getQueueUrl("admin");

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

    // Placeholder for admin message processing logic
    private void processMessage(String messageBody, Map<String, MessageAttributeValue> messageAttributes) {
        System.out.println("Admin message processed: " + messageBody);

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
            case "updatePlayerProfile":
                processUpdatePlayer(messageBody);
                break;
            case "createTournament":
                processAddTournament(messageBody);
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
    private void processAddPlayer(String messageBody) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Parse JSON string into JsonNode
            JsonNode rootNode = objectMapper.readTree(messageBody);
    
            // Extract fields from JSON
            String playerId = rootNode.path("playerId").asText();
            String username = rootNode.path("username").asText(null);
            String email = rootNode.path("email").asText(null);
            String password = rootNode.path("password").asText(null);
            String role = rootNode.path("role").asText("player"); // Default to "player" if not provided
    
            // Create a new User object
            User user = new User();
            user.setId(Long.parseLong(playerId)); 
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(role);
    
            String result = userService.createUserWithId(user);
            System.out.println("add player" + result);
        } catch (Exception e) {
            System.out.println("Failed to parse player data from message: " + e.getMessage());
        }
    }

    // Process Update Player action 
    private void processUpdatePlayer(String messageBody) {
        try {
            // Parse the update player profile request
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(messageBody);

            // Extract player data from message
            Long playerId = rootNode.path("playerId").asLong();
            String playerName = rootNode.path("playerName").asText(null);
            String username = rootNode.path("username").asText(null);
            String email = rootNode.path("email").asText(null);
            String password = rootNode.path("password").asText(null);

            // Call the service to update player details
            String result = playerService.updatePlayerProfile(playerId, playerName, username, email, password);
        } catch (Exception e) {
            System.err.println("Failed to process update player message: " + e.getMessage());
        }
    }

    // Process Add Tournament
    private void processAddTournament(String messageBody) {
        try {
            // Parse the create tournament request
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(messageBody);

            // Extract tournament data from message
            String timestampStart = rootNode.path("timestampStart").asText();
            int tournamentSize = rootNode.path("tournamentSize").asInt();

            // Convert timestampStart to LocalDateTime
            LocalDateTime tournamentStartTime = LocalDateTime.parse(timestampStart);

            // Extract player IDs
            JsonNode playerIdsNode = rootNode.path("playerIds");
            List<Long> playerIds = new ArrayList<>();
            for (JsonNode playerIdNode : playerIdsNode) {
                playerIds.add(playerIdNode.asLong());
            }

            // Create a new Tournament object
            Tournament tournament = new Tournament();
            tournament.setTimestampStart(tournamentStartTime);
            tournament.setTournamentSize(tournamentSize);

            // Save the tournament via tournamentService
            Tournament savedTournament = tournamentService.createTournament(tournament);

            // For each playerId,  Save the tournament-player relationship
            for (Long playerId : playerIds) {
                String result = tournamentPlayerService.createTournamentPlayer(playerId, savedTournament.getTournamentId());
            }

            // Print the result
            System.out.println("Tournament created: " + savedTournament.getTournamentId());
        } catch (Exception e) {
            System.err.println("Failed to process create tournament message: " + e.getMessage());
        }
    }

    // Helper method to delete the message from the queue after processing
    private void deleteMessageFromQueue(String queueUrl, Message message) {
        DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(message.receiptHandle())
                .build();
        sqsService.getSqsClient().deleteMessage(deleteRequest);
        System.out.println("Admin Queue message processed and deleted.");
    }
}