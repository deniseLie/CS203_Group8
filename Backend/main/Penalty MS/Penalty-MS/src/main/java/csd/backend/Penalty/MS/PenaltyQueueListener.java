package csd.backend.Penalty.MS;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Service
public class PenaltyQueueListener {

    private final SqsService sqsService;
    private final PenaltyService penaltyService;

    @Autowired
    public PenaltyQueueListener(SqsService sqsService, PenaltyService penaltyService) {
        this.sqsService = sqsService;
        this.penaltyService = penaltyService;
    }

    // Listen for messages in the Penalty Queue
    public void listenToPenaltyQueue() {
        String queueUrl = sqsService.getQueueUrl("penalty");

        while (true) {
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(10)  // Process up to 10 messages at once
                    .waitTimeSeconds(20)      // Long polling for efficiency
                    .build();

            ReceiveMessageResponse response = sqsService.getSqsClient().receiveMessage(receiveMessageRequest);

            for (Message message : response.messages()) {
                System.out.println("Processing Penalty Queue message: " + message.body());

                // Your penalty handling logic here
                processPenaltyMessage(message.body());

                // Delete message after processing
                deleteMessageFromQueue(queueUrl, message);
            }
        }
    }

    // Placeholder for penalty message processing logic
    private void processPenaltyMessage(String messageBody) {
        System.out.println("Penalty message processed: " + messageBody);

        // Process the message and extract player details
        Map<String, String> playerData = parsePlayerDataFromMessage(messageBody);

        // ADD PLAYER
        if (playerData != null) {
            // Call addPlayerToPool with extracted data
            String playerId = playerData.get("playerId");
            String email = playerData.get("email");
            String queueStatus = playerData.getOrDefault("queueStatus", "available");

            penaltyService.addPlayerToPool(playerId, email, queueStatus);
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
        System.out.println("Penalty Queue message processed and deleted.");
    }
}
