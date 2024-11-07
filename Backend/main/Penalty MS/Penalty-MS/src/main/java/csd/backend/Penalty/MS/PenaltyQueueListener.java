package csd.backend.Penalty.MS;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.*;

@Service
public class PenaltyQueueListener {

    private final SqsService sqsService;

    public PenaltyQueueListener(SqsService sqsService) {
        this.sqsService = sqsService;
    }

    // Listen for messages in the Penalty Queue
    public void listenToPenaltyQueue() {
        String queueUrl = sqsService.getPenaltyQueueUrl();

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
                DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(message.receiptHandle())
                        .build();
                sqsService.getSqsClient().deleteMessage(deleteRequest);
                System.out.println("Penalty Queue message processed and deleted.");
            }
        }
    }

    // Placeholder for penalty message processing logic
    private void processPenaltyMessage(String messageBody) {
        // Implement your specific penalty processing logic here
        System.out.println("Penalty message processed: " + messageBody);
    }
}
