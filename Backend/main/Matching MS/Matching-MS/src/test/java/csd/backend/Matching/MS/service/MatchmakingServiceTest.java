package csd.backend.Matching.MS.service;
import csd.backend.Matching.MS.MatchmakingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MatchmakingServiceTest {

    @Mock
    private DynamoDbClient dynamoDbClient;

    @Mock
    private SqsClient sqsClient;

    @InjectMocks
    private MatchmakingService matchmakingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPlayerToPool_Success() {
        // Arrange
        PutItemResponse putItemResponse = mock(PutItemResponse.class);
        when(dynamoDbClient.putItem(any(PutItemRequest.class))).thenReturn(putItemResponse);

        // Act
        matchmakingService.addPlayerToPool("player1", "player1@example.com", "queue", 1);

        // Assert
        verify(dynamoDbClient, times(1)).putItem(any(PutItemRequest.class));
    }

    @Test
    void checkForMatch_NotEnoughPlayers_ReturnsFalse() {
        // Arrange
        when(dynamoDbClient.scan(any())).thenReturn(Collections.emptyList());

        // Act
        boolean result = matchmakingService.checkForMatch(1);

        // Assert
        assertTrue(!result);
    }

    @Test
    void processSqsMessages_ValidMessage_AddsPlayerToPool() {
        // Arrange
        Message message = Message.builder().body("{\"email\":\"player1@example.com\",\"playerName\":\"Player1\",\"queueStatus\":\"queue\"}").build();
        when(sqsClient.receiveMessage(any())).thenReturn(Collections.singletonList(message));

        // Act
        matchmakingService.processSqsMessages();

        // Assert
        verify(dynamoDbClient, times(1)).putItem(any(PutItemRequest.class));
    }
}
