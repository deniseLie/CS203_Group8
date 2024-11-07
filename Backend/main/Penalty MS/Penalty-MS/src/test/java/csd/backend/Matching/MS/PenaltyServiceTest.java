import csd.backend.Penalty.MS.PenaltyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PenaltyServiceTest {

    @Mock
    private DynamoDbClient dynamoDbClient;

    @InjectMocks
    private PenaltyService penaltyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPlayerToPool_ShouldAddPlayerSuccessfully() {
        // Arrange
        String playerName = "Player1";
        String email = "player1@example.com";
        String queueStatus = "available";

        // Act
        penaltyService.addPlayerToPool(playerName, email, queueStatus);

        // Assert
        verify(dynamoDbClient, times(1)).putItem(any(PutItemRequest.class));
    }

    @Test
    void banPlayer_ShouldBanPlayerAndUpdateCount() {
        // Arrange
        String playerName = "Player1";
        Map<String, AttributeValue> playerItem = new HashMap<>();
        playerItem.put("playerName", AttributeValue.builder().s(playerName).build());
        playerItem.put("banCount", AttributeValue.builder().n("0").build());
        playerItem.put("banUntil", AttributeValue.builder().n("0").build());

        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(GetItemResponse.builder().item(playerItem).build());

        // Act
        penaltyService.banPlayer(playerName);

        // Assert
        verify(dynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
    }

    @Test
    void checkPlayerStatus_ShouldReturnCorrectStatus() {
        // Arrange
        String playerName = "Player1";
        Map<String, AttributeValue> playerItem = new HashMap<>();
        playerItem.put("playerName", AttributeValue.builder().s(playerName).build());
        playerItem.put("queueStatus", AttributeValue.builder().s("banned").build());
        playerItem.put("banUntil", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis() + 60000)).build()); // 1 minute ban

        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(GetItemResponse.builder().item(playerItem).build());

        // Act
        Map<String, Object> status = penaltyService.checkPlayerStatus(playerName);

        // Assert
        assertEquals("banned", status.get("queueStatus"));
        assertTrue((long) status.get("remainingTime") > 0); // Remaining time should be greater than 0
    }

    @Test
    void unbanExpiredPlayers_ShouldUnbanPlayersCorrectly() {
        // Arrange
        String playerName = "Player1";
        Map<String, AttributeValue> playerItem = new HashMap<>();
        playerItem.put("playerName", AttributeValue.builder().s(playerName).build());
        playerItem.put("queueStatus", AttributeValue.builder().s("banned").build());
        playerItem.put("banUntil", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis() - 1000)).build()); // Expired ban

        when(dynamoDbClient.scan(any(ScanRequest.class))).thenReturn(ScanResponse.builder().items(List.of(playerItem)).build());

        // Act
        penaltyService.unbanExpiredPlayers();

        // Assert
        verify(dynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class)); // Ensure the player status is updated to available
    }

    @Test
    void checkPlayerStatus_PlayerNotFound_ShouldReturnError() {
        // Arrange
        String playerName = "NonExistentPlayer";

        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(GetItemResponse.builder().build()); // No item found

        // Act
        Map<String, Object> status = penaltyService.checkPlayerStatus(playerName);

        // Assert
        assertEquals("Player not found", status.get("error"));
    }
}
