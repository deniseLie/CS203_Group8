// package csd.backend.Matching.MS;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
// import software.amazon.awssdk.services.dynamodb.model.*;

// import java.util.*;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// class MatchmakingServiceTest {

//     @Mock
//     private DynamoDbClient dynamoDbClient;

//     @InjectMocks
//     private MatchmakingService matchmakingService;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void testAddPlayerToPool() {
//         // Mock the DynamoDB putItem response
//         PutItemResponse putItemResponse = PutItemResponse.builder().build();
//         when(dynamoDbClient.putItem(any(PutItemRequest.class))).thenReturn(putItemResponse);

//         // Call the method under test
//         matchmakingService.addPlayerToPool(123, 1);

//         // Verify that the putItem method was called
//         verify(dynamoDbClient, times(1)).putItem(any(PutItemRequest.class));
//     }

//     @Test
//     void testCheckPoolForMatch() {
//         // Mock the DynamoDB scan response with a player
//         Map<String, AttributeValue> player1 = new HashMap<>();
//         player1.put("playerId", AttributeValue.builder().n("1").build());
//         player1.put("rank", AttributeValue.builder().n("1").build());

//         List<Map<String, AttributeValue>> mockItems = List.of(player1);

//         // Mocking the ScanResponse
//         ScanResponse mockScanResponse = mock(ScanResponse.class);
//         when(mockScanResponse.items()).thenReturn(mockItems);

//         when(dynamoDbClient.scan(any(ScanRequest.class))).thenReturn(mockScanResponse);

//         // Call the method under test
//         List<Map<String, AttributeValue>> players = matchmakingService.checkPoolForMatch(1);

//         // Verify the size and content of the players list
//         assertEquals(1, players.size());
//         assertEquals("1", players.get(0).get("playerId").n());
//         assertEquals("1", players.get(0).get("rank").n());
//     }

//     @Test
//     void testRemovePlayersFromPool() {
//         // Mock the DynamoDB deleteItem response
//         DeleteItemResponse deleteItemResponse = DeleteItemResponse.builder().build();
//         when(dynamoDbClient.deleteItem(any(DeleteItemRequest.class))).thenReturn(deleteItemResponse);

//         // Prepare a list of players to remove
//         Map<String, AttributeValue> player1 = new HashMap<>();
//         player1.put("playerId", AttributeValue.builder().n("1").build());

//         List<Map<String, AttributeValue>> players = List.of(player1);

//         // Call the method under test
//         matchmakingService.removePlayersFromPool(players);

//         // Verify that the deleteItem method was called
//         verify(dynamoDbClient, times(1)).deleteItem(any(DeleteItemRequest.class));
//     }
// }
