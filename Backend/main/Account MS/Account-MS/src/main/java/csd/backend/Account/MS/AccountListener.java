package csd.backend.Account.MS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import csd.backend.Account.MS.DTO.PlayerProfileUpdateRequest;
import csd.backend.Account.MS.Model.Player.Player;
import csd.backend.Account.MS.Service.SqsService;
import csd.backend.Account.MS.Service.Player.PlayerService;
import csd.backend.Account.MS.Service.Tournament.TournamentService;

import java.util.*;

@Service
public class AccountListener {

    private final SqsService sqsService;
    private final PlayerService playerService;
    private final TournamentService tournamentService;

    @Autowired
    public AccountListener(SqsService sqsService, PlayerService playerService, TournamentService tournamentService) {
        this.sqsService = sqsService;
        this.playerService = playerService;
        this.tournamentService = tournamentService;
    }

    // Listen for messages in the Account Queue
    public void listenToAccountQueue() {
        String queueUrl = sqsService.getQueueUrl("account");

        while (true) {
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(10)  // Process up to 10 messages at once
                    .waitTimeSeconds(20)      // Long polling for efficiency
                    .build();

            ReceiveMessageResponse response = sqsService.getSqsClient().receiveMessage(receiveMessageRequest);

            for (Message message : response.messages()) {
                System.out.println("Processing Account Queue message: " + message.body());
                
                // Extract the message attributes
                Map<String, MessageAttributeValue> messageAttributes = message.messageAttributes();

                // Your handling logic here
                processAccountMessage(message.body(), messageAttributes);

                // Delete message after processing
                deleteMessageFromQueue(queueUrl, message);
            }

            // Add delay between polling cycles to avoid exhausting resources
            try {
                Thread.sleep(5000); // 5-second delay between polling
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Queue listener interrupted: " + e.getMessage());
            }
        }
    }

    // Process the message based on its actionType
    public void processAccountMessage(String messageBody, Map<String, MessageAttributeValue> messageAttributes) {
        String actionType = getActionTypeFromMessageAttributes(messageAttributes);

        // If actionType is not present, log and skip processing
        if (actionType == null) {
            System.err.println("No actionType found in the message attributes. Skipping message.");
            return;
        }

        // Process the message based on actionType
        switch (actionType) {
            case "addPlayer":
                processAddPlayer(messageBody);
                break;
            case "addMatch":
                processAddTournament(messageBody);
                break;
            default:
                System.err.println("Unknown action type: " + actionType);
                break;
        }
    }

    // Helper method to retrieve the actionType from the message attributes
    private String getActionTypeFromMessageAttributes(Map<String, MessageAttributeValue> messageAttributes) {
        if (messageAttributes != null && messageAttributes.containsKey("actionType")) {
            return messageAttributes.get("actionType").stringValue();
        }
        return null;
    }

    // Process Add Player action
    private void processAddPlayer(String messageBody) {
        // Parse the message body and create the Player object
        Player newPlayer = parsePlayerDataFromMessage(messageBody);

        // Call the PlayerService to save the new player to the database
        if (newPlayer != null) {
            playerService.registerUser(newPlayer);
            System.out.println("Player added to the database: " + newPlayer.getUsername());
        } else {
            System.err.println("Failed to process AddPlayer message due to parsing issues.");
        }
    }

    // Parse player data from JSON message body and create Player object
    private Player parsePlayerDataFromMessage(String messageBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Parse JSON string into JsonNode
            JsonNode rootNode = objectMapper.readTree(messageBody);

            // Extract fields from JSON and create a Player object
            String playerId = rootNode.path("playerId").asText();
            String username = rootNode.path("username").asText();

            Player newPlayer = new Player();
            newPlayer.setId(Long.parseLong(playerId));  // Parse playerId and set it as the Player ID
            newPlayer.setUsername(username); // Set username

            return newPlayer;
        } catch (Exception e) {
            System.err.println("Failed to parse player data from message: " + e.getMessage());
            return null; // Return null if parsing fails
        }
    }
    
    // Process Add Tournament action
    private void processAddTournament(String messageBody) {
        // Parse tournament data from message body
        Map<String, String> tournamentData = processMatchMessage(messageBody);

        if (tournamentData != null) {
            // Convert the values to appropriate types before passing to handleMatchCompletion
            try {
                Long playerId = Long.parseLong(tournamentData.get("playerId"));
                Long championId = Long.parseLong(tournamentData.get("championId"));
                double kdRate = Double.parseDouble(tournamentData.get("kdRate"));
                int finalPlacement = Integer.parseInt(tournamentData.get("finalPlacement"));
                int rankPoints = Integer.parseInt(tournamentData.get("rankPoints"));
                boolean isWin = Boolean.parseBoolean(tournamentData.get("isWin"));

                // Call the TournamentService to handle tournament creation and saving
                tournamentService.createAndSaveTournament(tournamentData);

                // Call the PlayerService to handle the match completion
                playerService.handleMatchCompletion(playerId, championId, kdRate, finalPlacement, rankPoints, isWin);
                System.out.println("Tournament details processed for player: " + playerId);
            } catch (NumberFormatException e) {
                System.err.println("Error processing tournament data: " + e.getMessage());
            }
        } else {
            System.err.println("Failed to process AddTournament message due to parsing issues.");
        }
    }

    // Parse match data from JSON message body and create Player object
    private Map<String, String> processMatchMessage(String messageBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> matchData = new HashMap<>();

        try {
            JsonNode rootNode = objectMapper.readTree(messageBody);

            // Extract relevant match data
            String playerId = rootNode.path("playerId").asText();
            String championId = rootNode.path("championId").asText();
            String kdRate = rootNode.path("kdRate").asText();
            String finalPlacement = rootNode.path("finalPlacement").asText();
            String rankPoints = rootNode.path("rankPoints").asText();
            String isWin = rootNode.path("isWin").asText();

            // Populate the map with match details
            matchData.put("playerId", playerId);
            matchData.put("championId", championId);
            matchData.put("kdRate", kdRate);
            matchData.put("finalPlacement", finalPlacement);
            matchData.put("rankPoints", rankPoints);
            matchData.put("isWin", isWin);

            return matchData;
        } catch (Exception e) {
            System.err.println("Failed to parse match data from message: " + e.getMessage());
            return null; // Return null if parsing fails
        }
    }

    // Helper method to delete the message from the queue after processing
    private void deleteMessageFromQueue(String queueUrl, Message message) {
        DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(message.receiptHandle())
                .build();
        sqsService.getSqsClient().deleteMessage(deleteRequest);
        System.out.println("Account Queue message processed and deleted.");
    }
}