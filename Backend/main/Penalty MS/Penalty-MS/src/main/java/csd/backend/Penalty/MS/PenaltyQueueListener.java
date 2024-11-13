package csd.backend.Penalty.MS;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Service
public class PenaltyQueueListener {

    private final SqsService sqsService;
    private final PenaltyService penaltyService;

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

                // Extract the message attributes from the received message
                Map<String, MessageAttributeValue> messageAttributes = message.messageAttributes();

                // Your penalty handling logic here
                processPenaltyMessage(message.body(), messageAttributes);

                // Delete message after processing
                deleteMessageFromQueue(queueUrl, message);
            }
        }
    }

    // Process penalty message and add player to pool
    private void processPenaltyMessage(String messageBody, Map<String, MessageAttributeValue> messageAttributes) {
        System.out.println("Penalty message processed: " + messageBody);

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
            case "deletePlayerProfile":
                processDeletePlayer(messageBody);
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

    // Process add player to db
    private void processAddPlayer (String messageBody) {
        try {
            // Parse JSON string into JsonNode
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(messageBody);

            // Extract player details from the JSON message
            long  playerId = Long.parseLong(rootNode.path("playerId").asText());
            String queueStatus = rootNode.path("queueStatus").asText("available");

            // Call penalty service to add player to pool
            penaltyService.addPlayerToPool(playerId, queueStatus);

        } catch (Exception e) {
            System.out.println("Failed to process penalty message: " + e.getMessage());
        }
    }

    // Process deete player 
    private void processDeletePlayer (String messageBody) {
        try {
            // Parse JSON string into JsonNode
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(messageBody);

            // Extract player details from the JSON message
            Long playerId = Long.parseLong(rootNode.path("playerId").asText());

            // Call penalty service to delete player from pool
            if (playerId != null) {
                penaltyService.deletePlayer(playerId);
                System.out.println("Player profile updated for playerId: " + playerId);
            } else {
                System.err.println("Player ID not found in message.");
            }

        } catch (Exception e) {
            System.out.println("Failed to process penalty message: " + e.getMessage());
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
