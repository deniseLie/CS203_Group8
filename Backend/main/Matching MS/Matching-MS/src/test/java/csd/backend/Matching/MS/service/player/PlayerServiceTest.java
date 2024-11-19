package csd.backend.Matching.MS.service.player;

import csd.backend.Matching.MS.exception.PlayerNotFoundException;
import csd.backend.Matching.MS.exception.PlayerUpdateException;
import csd.backend.Matching.MS.model.PlayerStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlayerServiceTest {

    @Mock
    private DynamoDbClient dynamoDbClient;

    @InjectMocks
    private PlayerService playerService;

    private static final Long PLAYER_ID = 1L;
    private static final String QUEUE_STATUS = PlayerStatus.QUEUE.getStatus();
    private static final Long CHAMPION_ID = 5L;
    private static final Long RANK_ID = 5L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test: Get Player by ID - Success
     */
    @Test
    void testGetPlayerById_Success() {
        // Arrange
        Map<String, AttributeValue> playerData = new HashMap<>();
        playerData.put("playerId", AttributeValue.builder().n(String.valueOf(PLAYER_ID)).build());
        playerData.put("queueStatus", AttributeValue.builder().s(QUEUE_STATUS).build());

        GetItemResponse mockResponse = GetItemResponse.builder().item(playerData).build();
        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(mockResponse);

        // Act
        Map<String, AttributeValue> result = playerService.getPlayerById(PLAYER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(String.valueOf(PLAYER_ID), result.get("playerId").n());
        assertEquals(QUEUE_STATUS, result.get("queueStatus").s());
        verify(dynamoDbClient, times(1)).getItem(any(GetItemRequest.class));
    }

    /**
     * Test: Get Player by ID - Player Not Found
     */
    @Test
    void testGetPlayerById_PlayerNotFound() {
        // Arrange
        // Mock a response where an empty map is returned (simulating "player not found")
        GetItemResponse mockResponse = GetItemResponse.builder().item(Collections.emptyMap()).build();
        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        PlayerNotFoundException exception = assertThrows(PlayerNotFoundException.class, () -> 
            playerService.getPlayerById(PLAYER_ID)
        );

        // Assert that the exception message is correct
        assertEquals("Player with ID " + PLAYER_ID + " was not found in the database.", exception.getMessage());

        // Verify that the `getItem` method is called exactly once
        verify(dynamoDbClient, times(1)).getItem(any(GetItemRequest.class));
    }

    /**
     * Test: Get Player Rank ID - Unsuccessful
     */
    @Test
    void testGetPlayerRankId_Unsuccessful() {
        // Arrange
        GetItemResponse mockResponse = GetItemResponse.builder().item(Collections.emptyMap()).build();
        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        PlayerNotFoundException exception = assertThrows(PlayerNotFoundException.class, () -> 
            playerService.getPlayerRankId(PLAYER_ID)
        );

        assertEquals("Player with ID " + PLAYER_ID + " was not found in the database.", exception.getMessage());
        verify(dynamoDbClient, times(1)).getItem(any(GetItemRequest.class));
    }

    /**
     * Test: Check Player Status - Banned Player
     */
    @Test
    void testCheckPlayerStatus_BannedPlayer() {
        // Arrange
        long banUntil = System.currentTimeMillis() + 60000; // 1 minute from now
        Map<String, AttributeValue> playerData = new HashMap<>();
        playerData.put("playerId", AttributeValue.builder().n(String.valueOf(PLAYER_ID)).build());
        playerData.put("queueStatus", AttributeValue.builder().s(QUEUE_STATUS).build());
        playerData.put("banUntil", AttributeValue.builder().n(String.valueOf(banUntil)).build());

        GetItemResponse mockResponse = GetItemResponse.builder().item(playerData).build();
        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(mockResponse);

        // Act
        Map<String, Object> status = playerService.checkPlayerStatus(PLAYER_ID);

        // Assert
        assertNotNull(status);
        assertEquals(String.valueOf(PLAYER_ID), status.get("playerId"));
        assertEquals(QUEUE_STATUS, status.get("queueStatus"));
        assertTrue((long) status.get("remainingTime") > 0);
    }

    /**
     * Test: Check Player Status - Ban Expired
     */
    @Test
    void testCheckPlayerStatus_BanExpired() {
        // Arrange
        long banUntil = System.currentTimeMillis() - 1000; // 1 second in the past
        Map<String, AttributeValue> playerData = new HashMap<>();
        playerData.put("playerId", AttributeValue.builder().n(String.valueOf(PLAYER_ID)).build());
        playerData.put("queueStatus", AttributeValue.builder().s(QUEUE_STATUS).build());
        playerData.put("banUntil", AttributeValue.builder().n(String.valueOf(banUntil)).build());

        GetItemResponse mockResponse = GetItemResponse.builder().item(playerData).build();
        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(mockResponse);

        // Act
        Map<String, Object> status = playerService.checkPlayerStatus(PLAYER_ID);

        // Assert
        assertNotNull(status);
        assertEquals(0L, status.get("remainingTime")); // Ban expired, remaining time is 0
        verify(dynamoDbClient, times(1)).getItem(any(GetItemRequest.class));
    }


    /**
     * Test: Check and Handle Player Ban - Unsuccessful (Player Not Found)
     */
    @Test
    void testCheckAndHandlePlayerBan_PlayerNotFound() {
        // Arrange
        GetItemResponse mockResponse = GetItemResponse.builder().item(Collections.emptyMap()).build();
        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        PlayerNotFoundException exception = assertThrows(PlayerNotFoundException.class, () -> 
            playerService.checkAndHandlePlayerBan(PLAYER_ID)
        );

        assertEquals("Player with ID " + PLAYER_ID + " was not found in the database.", exception.getMessage());
        verify(dynamoDbClient, times(1)).getItem(any(GetItemRequest.class));
    }
    
    /**
     * Test: Check Players in Queue - Success
     */
    @Test
    void testCheckPlayersInQueue_Success() {
        // Arrange
        List<Map<String, AttributeValue>> mockPlayers = List.of(
            Map.of("playerId", AttributeValue.builder().n("1").build()),
            Map.of("playerId", AttributeValue.builder().n("2").build())
        );
        ScanResponse mockResponse = ScanResponse.builder().items(mockPlayers).build();
        when(dynamoDbClient.scan(any(ScanRequest.class))).thenReturn(mockResponse);

        // Act
        List<Map<String, AttributeValue>> players = playerService.checkPlayersInQueue(RANK_ID);

        // Assert
        assertNotNull(players);
        assertEquals(2, players.size());
        verify(dynamoDbClient, times(1)).scan(any(ScanRequest.class));
    }

    /**
     * Test: Check Players in Speed-Up Queue - Success
     */
    @Test
    void testCheckPlayersInSpeedUpQueue_Success() {
        // Arrange
        List<Map<String, AttributeValue>> mockPlayers = List.of(
            Map.of("playerId", AttributeValue.builder().n("1").build()),
            Map.of("playerId", AttributeValue.builder().n("2").build())
        );
        ScanResponse mockResponse = ScanResponse.builder().items(mockPlayers).build();
        when(dynamoDbClient.scan(any(ScanRequest.class))).thenReturn(mockResponse);

        // Act
        List<Map<String, AttributeValue>> players = playerService.checkPlayersInSpeedUpQueue(RANK_ID);

        // Assert
        assertNotNull(players);
        assertEquals(2, players.size());
        verify(dynamoDbClient, times(1)).scan(any(ScanRequest.class));
    }

    /**
     * Test: Add Player to Pool - Success
     */
    @Test
    void testAddPlayerToPool_Success() {
        // Arrange
        PutItemResponse mockResponse = PutItemResponse.builder().build();
        when(dynamoDbClient.putItem(any(PutItemRequest.class))).thenReturn(mockResponse);

        // Act
        playerService.addPlayerToPool(PLAYER_ID, QUEUE_STATUS, RANK_ID.intValue());

        // Assert
        verify(dynamoDbClient, times(1)).putItem(any(PutItemRequest.class));
    }

    /**
     * Test: Add Player to Pool - Failure
     */
    @Test
    void testAddPlayerToPool_Failure() {
        // Arrange
        doThrow(RuntimeException.class).when(dynamoDbClient).putItem(any(PutItemRequest.class));

        // Act & Assert
        assertThrows(PlayerUpdateException.class, () ->
                playerService.addPlayerToPool(PLAYER_ID, QUEUE_STATUS, RANK_ID.intValue()));
        verify(dynamoDbClient, times(1)).putItem(any(PutItemRequest.class));
    }

    /**
     * Test: Update Player Rank - Success
     */
    @Test
    void testUpdatePlayerRank_Success() {
        // Arrange
        UpdateItemResponse mockResponse = UpdateItemResponse.builder().build();
        when(dynamoDbClient.updateItem(any(UpdateItemRequest.class))).thenReturn(mockResponse);

        // Act
        playerService.updatePlayerRank(PLAYER_ID, RANK_ID);

        // Assert
        verify(dynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
    }

    /**
     * Test: Update Player Rank - Failure
     */
    @Test
    void testUpdatePlayerRank_Failure() {
        // Arrange
        doThrow(RuntimeException.class).when(dynamoDbClient).updateItem(any(UpdateItemRequest.class));

        // Act & Assert
        assertThrows(PlayerUpdateException.class, () -> playerService.updatePlayerRank(PLAYER_ID, RANK_ID));
        verify(dynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
    }

    /**
     * Test: Update Player Rank - Null Player ID
     */
    @Test
    void testUpdatePlayerRank_NullPlayerId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> playerService.updatePlayerRank(null, RANK_ID));
    }

    /**
     * Test: Update Player Rank - Null Rank ID
     */
    @Test
    void testUpdatePlayerRank_NullRankId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            playerService.updatePlayerRank(PLAYER_ID, null)
        );

        assertEquals("Rank ID cannot be null", exception.getMessage());
    }


    /**
     * Test: Update Player Status - Success
     */
    @Test
    void testUpdatePlayerStatus_Success() {
        // Arrange
        UpdateItemResponse mockResponse = UpdateItemResponse.builder().build();
        when(dynamoDbClient.updateItem(any(UpdateItemRequest.class))).thenReturn(mockResponse);

        // Act
        playerService.updatePlayerStatus(PLAYER_ID, QUEUE_STATUS);

        // Assert
        verify(dynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
    }

    /**
     * Test: Update Player Status - Failure
     */
    @Test
    void testUpdatePlayerStatus_Failure() {
        // Arrange
        doThrow(RuntimeException.class).when(dynamoDbClient).updateItem(any(UpdateItemRequest.class));

        // Act & Assert
        assertThrows(PlayerUpdateException.class, () -> playerService.updatePlayerStatus(PLAYER_ID, QUEUE_STATUS));
        verify(dynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
    }
    
    /**
     * Test: Update Player Champion - Successful
     */
    @Test
    void testUpdatePlayerChampion_Success() {
        // Arrange
        UpdateItemResponse mockResponse = UpdateItemResponse.builder().build();
        when(dynamoDbClient.updateItem(any(UpdateItemRequest.class))).thenReturn(mockResponse);

        // Act
        assertDoesNotThrow(() -> playerService.updatePlayerChampion(PLAYER_ID, CHAMPION_ID));

        // Assert
        verify(dynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
    }

    /**
     * Test: Update Player Champion - Unsuccessful
     */
    @Test
    void testUpdatePlayerChampion_Unsuccessful() {
        // Arrange
        doThrow(RuntimeException.class).when(dynamoDbClient).updateItem(any(UpdateItemRequest.class));

        // Act & Assert
        assertThrows(PlayerUpdateException.class, () -> 
            playerService.updatePlayerChampion(PLAYER_ID, CHAMPION_ID)
        );

        verify(dynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
    }

    /**
     * Test: Update Player Champion - Null Champion ID
     */
    @Test
    void testUpdatePlayerChampion_NullChampionId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> playerService.updatePlayerChampion(PLAYER_ID, null));
    }

    /**
     * Test: Remove Player Champion - Success
     */
    @Test
    void testRemovePlayerChampion_Success() {
        // Arrange
        UpdateItemResponse mockResponse = UpdateItemResponse.builder().build();
        when(dynamoDbClient.updateItem(any(UpdateItemRequest.class))).thenReturn(mockResponse);

        // Act
        playerService.removePlayerChampion(PLAYER_ID);

        // Assert
        verify(dynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
    }

    /**
     * Test: Remove Player Champion - Failure
     */
    @Test
    void testRemovePlayerChampion_Failure() {
        // Arrange
        doThrow(RuntimeException.class).when(dynamoDbClient).updateItem(any(UpdateItemRequest.class));

        // Act & Assert
        assertThrows(PlayerUpdateException.class, () -> playerService.removePlayerChampion(PLAYER_ID));
        verify(dynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
    }
    
    /**
     * Test: Update Player Ban Status - Success
     */
    @Test
    void testUpdatePlayerBanStatus_Success() {
        // Arrange
        UpdateItemResponse mockResponse = UpdateItemResponse.builder().build();
        when(dynamoDbClient.updateItem(any(UpdateItemRequest.class))).thenReturn(mockResponse);

        // Act
        playerService.updatePlayerBanStatus(PLAYER_ID, PlayerStatus.BAN.getStatus(), System.currentTimeMillis() + 60000);

        // Assert
        verify(dynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
    }

    /**
     * Test: Update Player Ban Status - Unsuccessful
     */
    @Test
    void testUpdatePlayerBanStatus_Unsuccessful() {
        // Arrange
        long banEndTime = System.currentTimeMillis() + 60000; // 1 minute from now
        doThrow(RuntimeException.class).when(dynamoDbClient).updateItem(any(UpdateItemRequest.class));

        // Act & Assert
        assertThrows(PlayerUpdateException.class, () -> 
            playerService.updatePlayerBanStatus(PLAYER_ID, QUEUE_STATUS, banEndTime)
        );

        verify(dynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
    }

    /**
     * Test: Delete Player - Success
     */
    @Test
    void testDeletePlayer_Success() {
        // Arrange
        DeleteItemResponse mockResponse = DeleteItemResponse.builder().build();
        when(dynamoDbClient.deleteItem(any(DeleteItemRequest.class))).thenReturn(mockResponse);

        // Act
        playerService.deletePlayer(PLAYER_ID);

        // Assert
        verify(dynamoDbClient, times(1)).deleteItem(any(DeleteItemRequest.class));
    }

    /**
     * Test: Delete Player - Failure
     */
    @Test
    void testDeletePlayer_Failure() {
        // Arrange
        doThrow(RuntimeException.class).when(dynamoDbClient).deleteItem(any(DeleteItemRequest.class));

        // Act & Assert
        assertThrows(PlayerUpdateException.class, () -> playerService.deletePlayer(PLAYER_ID));
        verify(dynamoDbClient, times(1)).deleteItem(any(DeleteItemRequest.class));
    }

}
