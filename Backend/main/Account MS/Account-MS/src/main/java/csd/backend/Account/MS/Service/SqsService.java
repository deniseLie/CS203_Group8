package csd.backend.Account.MS.service;

import java.util.Map;

// import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
public class SqsService {

    private final SqsClient sqsClient;

    // private final Dotenv dotenv = Dotenv.load();

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
            case "login":
                return accountQueueUrl;
            default:
                throw new IllegalArgumentException("Invalid queue name");
        }
    }

    // Send message to SQS Queue
    public void sendMessageToQueue(String queueName, String messageBody, Map<String, MessageAttributeValue> messageAttributes) {
        String queueUrl = getQueueUrl(queueName);

        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .messageAttributes(messageAttributes)
                .messageGroupId("MessageGroup1") // Required for FIFO queues
                .messageDeduplicationId(String.valueOf(messageBody.hashCode())) // Ensures unique messages in FIFO
                .build();

        SendMessageResponse response = sqsClient.sendMessage(sendMsgRequest);
        System.out.println("Message sent to " + queueName + " queue with MessageId: " + response.messageId());
    }
    
    // Close the SQS client on shutdown
    public void close() {
        sqsClient.close();
    }
}