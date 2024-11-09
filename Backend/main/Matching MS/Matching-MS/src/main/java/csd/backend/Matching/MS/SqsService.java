package csd.backend.Matching.MS;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.*;

@Service
public class SqsService {

    private final SqsClient sqsClient;

    private final Dotenv dotenv = Dotenv.load();
    private final String accountQueueUrl = dotenv.get("ACCOUNT_QUEUE_URL");
    private final String matchmakingQueueUrl = dotenv.get("MATCHMAKING_QUEUE_URL");
    private final String penaltyQueueUrl = dotenv.get("PENALTY_QUEUE_URL");

    public SqsService() {
        if (accountQueueUrl == null || matchmakingQueueUrl == null || penaltyQueueUrl == null) {
            throw new IllegalStateException("Required environment variables are missing.");
        }
        System.out.println("Loaded environment variables successfully.");
        this.sqsClient = SqsClient.builder().build();
    }

    public SqsClient getSqsClient () {
        return sqsClient;
    }

    // Get Queue URL based on name
    public String getQueueUrl(String queueName) {
        switch (queueName) {
            case "matchmaking":
                return matchmakingQueueUrl;
            case "penalty":
                return penaltyQueueUrl;
            case "account":
                return accountQueueUrl;
            default:
                throw new IllegalArgumentException("Invalid queue name");
        }
    }

    // Send message to SQS Queue with error handling
    public void sendMessageToQueue(String queueName, String messageBody, Map<String, MessageAttributeValue> messageAttributes) {
        String queueUrl = getQueueUrl(queueName);

        SendMessageRequest.Builder sendMsgRequestBuilder = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .messageAttributes(messageAttributes);

        // Add FIFO queue parameters if applicable
        if (queueUrl.contains(".fifo")) {
            sendMsgRequestBuilder.messageGroupId("MessageGroup1")
                    .messageDeduplicationId(String.valueOf(messageBody.hashCode()));
        }

        try {
            SendMessageResponse response = sqsClient.sendMessage(sendMsgRequestBuilder.build());
            System.out.println("Message sent to " + queueName + " queue with MessageId: " + response.messageId());
        } catch (Exception e) {
            System.err.println("Failed to send message to " + queueName + " queue: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send message to " + queueName, e);
        }
    }

    // Close the SQS client on shutdown
    public void close() {
        sqsClient.close();
    }
}
