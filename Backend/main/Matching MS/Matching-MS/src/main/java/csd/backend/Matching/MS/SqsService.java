package csd.backend.Matching.MS;

import org.springframework.beans.factory.annotation.Value;
// import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.*;

@Service
public class SqsService {

    private final SqsClient sqsClient;

    @Value("${account.queue.url}")
    private String accountQueueUrl;

    @Value("${matchmaking.queue.url}")
    private String matchmakingQueueUrl;

    @Value("${penalty.queue.url}")
    private String penaltyQueueUrl;

    public SqsService() {
        this.sqsClient = SqsClient.builder().build();
    }

    // Method to get the SqsClient instance
    public SqsClient getSqsClient() {
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
