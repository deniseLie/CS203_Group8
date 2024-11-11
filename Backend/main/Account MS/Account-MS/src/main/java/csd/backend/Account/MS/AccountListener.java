package csd.backend.Account.MS;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import csd.backend.Account.MS.Model.AppUser;
import csd.backend.Account.MS.Model.AppUserService;

import java.util.*;

@Service
public class AccountListener {

    private final SqsService sqsService;
    private final AppUserService appUserService;

    @Autowired
    public AccountListener(SqsService sqsService, AppUserService appUserService) {
        this.sqsService = sqsService;
        this.appUserService = appUserService;
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

                // Your handling logic here
                processAccountMessage(message.body());

                // Delete message after processing
                deleteMessageFromQueue(queueUrl, message);
            }
        }
    }

    // Placeholder for message processing logic
    private void processAccountMessage(String messageBody) {
        System.out.println("Account message processed: " + messageBody);

        // Process the message and extract player details
        Map<String, String> playerData = parsePlayerDataFromMessage(messageBody);

        // ADD PLAYER
        if (playerData != null) {
            // Call addPlayerToPool with extracted data
            String playerId = playerData.get("playerId");
            String email = playerData.get("email");

            // appUserService.registerUser(new AppUser(playerId, email));
            System.out.println("Player added to pool: " + playerId);
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