package csd.backend.Penalty.MS;

import java.util.Map;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
public class SqsService {

    private static final SqsClient sqsClient = SqsClient.builder().build();
    private static final String PENALTY_QUEUE_URL = "https://sqs.ap-southeast-1.amazonaws.com/123123/Penalty-Queue.fifo";

    // Method to get the SqsClient instance
    public SqsClient getSqsClient() {
        return sqsClient;
    }

    // Method to get the Penalty Queue URL
    public String getPenaltyQueueUrl() {
        return PENALTY_QUEUE_URL;
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
