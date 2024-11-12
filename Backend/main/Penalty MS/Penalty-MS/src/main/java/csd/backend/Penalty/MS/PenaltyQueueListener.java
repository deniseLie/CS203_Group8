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

    // Process penalty message and add player to pool
    private void processPenaltyMessage(String messageBody) {
        System.out.println("Penalty message processed: " + messageBody);

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
