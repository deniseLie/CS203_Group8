package com.loltournament.loginservice.service;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SqsService {
    private final SqsClient sqsClient;
    public final String matchmakingQueueUrl = "https://sqs.ap-southeast-1.amazonaws.com/908027379110/Matchmatching-Queue.fifo";
    public final String penaltyQueueUrl = "https://sqs.ap-southeast-1.amazonaws.com/908027379110/Penalty-Queue.fifo";
    public final String accountQueueUrl = "https://sqs.ap-southeast-1.amazonaws.com/908027379110/Account-Queue.fifo";
    public final String adminQueueUrl = "https://sqs.ap-southeast-1.amazonaws.com/908027379110/Admin-Queue.fifo";

    public SqsService() {
        this.sqsClient = SqsClient.builder().build();
    }

    public void sendMessageToQueue(String queueUrl, String messageBody, String messageGroupId) {
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
            .queueUrl(queueUrl)
            .messageBody(messageBody)
            .messageGroupId(messageGroupId)
            .build();
        sqsClient.sendMessage(sendMsgRequest);
        System.out.println("Message sent to queue: " + queueUrl);
    }

    public List<Message> receiveMessages(String queueUrl) {
        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
            .queueUrl(queueUrl)
            .maxNumberOfMessages(10)
            .waitTimeSeconds(20)  // Long polling for efficiency
            .build();

        return sqsClient.receiveMessage(receiveRequest).messages();
    }

    public void deleteMessage(String queueUrl, String receiptHandle) {
        DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
            .queueUrl(queueUrl)
            .receiptHandle(receiptHandle)
            .build();
        sqsClient.deleteMessage(deleteRequest);
        System.out.println("Message deleted from queue: " + queueUrl);
    }
}

