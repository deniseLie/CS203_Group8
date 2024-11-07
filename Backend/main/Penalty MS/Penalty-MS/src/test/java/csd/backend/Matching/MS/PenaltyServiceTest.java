// package csd.backend.Penalty.MS;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// import csd.backend.Penalty.MS.PenaltyService;
// import csd.backend.Penalty.MS.SqsService;
// import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
// import software.amazon.awssdk.services.dynamodb.model.*;
// import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
// import software.amazon.awssdk.services.sqs.SqsClient;

// import java.util.*;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// class PenaltyServiceTest {

//     @Mock
//     private DynamoDbClient dynamoDbClient;

//     @Mock
//     private SqsClient sqsClient;

//     @Mock
//     private SqsService sqsService;

//     @InjectMocks
//     private PenaltyService penaltyService;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);

//         // Mock the SqsClient in SqsService
//         when(sqsService.getSqsClient()).thenReturn(sqsClient);
//         when(sqsService.getPenaltyQueueUrl()).thenReturn("https://sqs.ap-southeast-1.amazonaws.com/123123/Penalty-Queue.fifo");
//     }

//     @Test
//     void addPlayerToPool_ShouldAddPlayerSuccessfully() {
//         // Arrange
//         String playerName = "Player1";
//         String email = "player1@example.com";
//         String queueStatus = "available";

//         // Act
//         penaltyService.addPlayerToPool(playerName, email, queueStatus);

//         // Assert
//         verify(dynamoDbClient, times(1)).putItem(any(PutItemRequest.class));
//     }

//     @Test
//     void banPlayer_ShouldBanPlayerAndUpdateCount() {
//         // Arrange
//         String playerName = "Player1";
//         Map<String, AttributeValue> playerItem = new HashMap<>();
//         playerItem.put("playerName", AttributeValue.builder().s(playerName).build());
//         playerItem.put("banCount", AttributeValue.builder().n("0").build());
//         playerItem.put("banUntil", AttributeValue.builder().n("0").build());

//         when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(GetItemResponse.builder().item(playerItem).build());

//         // Act
//         penaltyService.banPlayer(playerName);

//         // Assert
//         verify(dynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
//     }

//     @Test
//     void checkPlayerStatus_ShouldReturnCorrectStatus_WhenPlayerIsBanned() {
//         // Arrange
//         String playerName = "Player1";
//         Map<String, AttributeValue> playerItem = new HashMap<>();
//         playerItem.put("playerName", AttributeValue.builder().s(playerName).build());
//         playerItem.put("queueStatus", AttributeValue.builder().s("banned").build());
//         playerItem.put("banUntil", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis() + 60000)).build()); // 1 minute ban

//         when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(GetItemResponse.builder().item(playerItem).build());

//         // Act
//         Map<String, Object> status = penaltyService.checkPlayerStatus(playerName);

//         // Assert
//         assertEquals("banned", status.get("queueStatus"));
//         assertTrue((long) status.get("remainingTime") > 0);
//     }

//     @Test
//     void checkPlayerStatus_ShouldReturnError_WhenPlayerNotFound() {
//         // Arrange
//         String playerName = "NonExistentPlayer";

//         // Mocking DynamoDbClient response to simulate "player not found"
//         // Returning empty response
//         when(dynamoDbClient.getItem(any(GetItemRequest.class)))
//             .thenReturn(GetItemResponse.builder().build());     

//         // Act
//         Map<String, Object> status = penaltyService.checkPlayerStatus(playerName);

//         // Assert
//         assertEquals("Player not found", status.get("error"));
//     }

//     @Test
//     void unbanExpiredPlayers_ShouldUnbanPlayersCorrectly() {
//         // Arrange
//         String playerName = "Player1";
//         Map<String, AttributeValue> playerItem = new HashMap<>();
//         playerItem.put("playerName", AttributeValue.builder().s(playerName).build());
//         playerItem.put("queueStatus", AttributeValue.builder().s("banned").build());
//         playerItem.put("banUntil", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis() - 1000)).build()); // Expired ban

//         when(dynamoDbClient.scan(any(ScanRequest.class)))
//             .thenReturn(ScanResponse.builder().items(Collections.singletonList(playerItem)).build());

//         // Act
//         penaltyService.unbanExpiredPlayers();

//         // Assert
//         verify(dynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
//     }

//     @Test
//     void sendMessageToPenaltyQueue_ShouldSendMessageSuccessfully() {
//         // Arrange
//         String messageBody = "Test message";
//         Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
//         messageAttributes.put("AttributeKey", MessageAttributeValue.builder().stringValue("AttributeValue").build());

//         // Act
//         penaltyService.sendMessageToPenaltyQueue(messageBody, messageAttributes);

//         // Assert
//         verify(sqsService, times(1)).getPenaltyQueueUrl();
//         // verify(sqsClient, times(1)).sendMessage(any(SendMessageRequest.class));
//     }
// }
