package csd.backend.Penalty.MS.service;

import csd.backend.Penalty.MS.exception.PlayerNotFoundException;
import csd.backend.Penalty.MS.exception.PlayerUpdateException;
import csd.backend.Penalty.MS.service.PlayerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlayerServiceTest {

    @Mock
    private DynamoDbClient dynamoDbClient;

    @InjectMocks
    private PlayerService playerService;

    private static final Long PLAYER_ID = 1L;
    private static final String QUEUE_STATUS = "available";
    private static final String BANNED_STATUS = "banned";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*
     * Test add player - successfull
     */
    @Test
    void testAddPlayerToPool_Success() {
        // Arrange
        PutItemResponse mockResponse = PutItemResponse.builder().build();
        when(dynamoDbClient.putItem(any(PutItemRequest.class))).thenReturn(mockResponse);

        // Act
        assertDoesNotThrow(() -> playerService.addPlayerToPool(PLAYER_ID, QUEUE_STATUS));

        // Assert
        verify(dynamoDbClient, times(1)).putItem(any(PutItemRequest.class));
    }

    /*
     * Test get player details - success
     */
    @Test
    void testGetPlayerDetails_Success() {
        // Arrange
        Map<String, AttributeValue> mockPlayer = Map.of(
                "playerId", AttributeValue.builder().n(String.valueOf(PLAYER_ID)).build(),
                "queueStatus", AttributeValue.builder().s(QUEUE_STATUS).build()
        );
        GetItemResponse mockResponse = GetItemResponse.builder().item(mockPlayer).build();
        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(mockResponse);

        // Act
        Map<String, AttributeValue> result = playerService.getPlayerDetails(PLAYER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(QUEUE_STATUS, result.get("queueStatus").s());
        verify(dynamoDbClient, times(1)).getItem(any(GetItemRequest.class));
    }

    /*
     * Test get player details - player not found
     */
    @Test
    void testGetPlayerDetails_PlayerNotFound() {
        // Arrange
        GetItemResponse mockResponse = GetItemResponse.builder().item(Collections.emptyMap()).build();
        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        assertThrows(PlayerNotFoundException.class, () -> playerService.getPlayerDetails(PLAYER_ID));
        verify(dynamoDbClient, times(1)).getItem(any(GetItemRequest.class));
    }

    /*
     * Test get ban count - success
     */
    @Test
    void testGetBanCount_Success() {
        // Arrange
        Map<String, AttributeValue> mockPlayer = Map.of(
                "playerId", AttributeValue.builder().n(String.valueOf(PLAYER_ID)).build(),
                "banCount", AttributeValue.builder().n("3").build()
        );
        GetItemResponse mockResponse = GetItemResponse.builder().item(mockPlayer).build();
        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(mockResponse);

        // Act
        int banCount = playerService.getBanCount(PLAYER_ID);

        // Assert
        assertEquals(3, banCount);
        verify(dynamoDbClient, times(1)).getItem(any(GetItemRequest.class));
    }

    /*
     * Test get banned players - successs
     */
    @Test
    void testGetBannedPlayers_Success() {
        // Arrange
        List<Map<String, AttributeValue>> mockPlayers = List.of(
                Map.of("playerId", AttributeValue.builder().n("1").build()),
                Map.of("playerId", AttributeValue.builder().n("2").build())
        );
        ScanResponse mockResponse = ScanResponse.builder().items(mockPlayers).build();
        when(dynamoDbClient.scan(any(ScanRequest.class))).thenReturn(mockResponse);

        // Act
        List<Map<String, AttributeValue>> result = playerService.getBannedPlayers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(dynamoDbClient, times(1)).scan(any(ScanRequest.class));
    }

    /*
     * Test update player status - success
     */
    @Test
    void testUpdatePlayerStatus_Success() {
        // Arrange
        UpdateItemResponse mockResponse = UpdateItemResponse.builder().build();
        when(dynamoDbClient.updateItem(any(UpdateItemRequest.class))).thenReturn(mockResponse);

        // Act
        assertDoesNotThrow(() -> playerService.updatePlayerStatus(PLAYER_ID, BANNED_STATUS));

        // Assert
        verify(dynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
    }

    /*
     * Test update ban until - success
     */
    @Test
    void testUpdateBanUntil_Success() {
        // Arrange
        UpdateItemResponse mockResponse = UpdateItemResponse.builder().build();
        when(dynamoDbClient.updateItem(any(UpdateItemRequest.class))).thenReturn(mockResponse);

        // Act
        assertDoesNotThrow(() -> playerService.updateBanUntil(PLAYER_ID, System.currentTimeMillis()));

        // Assert
        verify(dynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
    }

    /*
     * Test update ban count - success
     */
    @Test
    void testUpdateBanCount_Success() {
        // Arrange
        UpdateItemResponse mockResponse = UpdateItemResponse.builder().build();
        when(dynamoDbClient.updateItem(any(UpdateItemRequest.class))).thenReturn(mockResponse);

        // Act
        assertDoesNotThrow(() -> playerService.updateBanCount(PLAYER_ID, 5));

        // Assert
        verify(dynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
    }

    /*
     * test delete player - success
     */
    @Test
    void testDeletePlayer_Success() {
        // Arrange
        DeleteItemResponse mockResponse = DeleteItemResponse.builder().build();
        when(dynamoDbClient.deleteItem(any(DeleteItemRequest.class))).thenReturn(mockResponse);

        // Act
        assertDoesNotThrow(() -> playerService.deletePlayer(PLAYER_ID));

        // Assert
        verify(dynamoDbClient, times(1)).deleteItem(any(DeleteItemRequest.class));
    }

    /*
     * Test delete player - failure
     */
    @Test
    void testDeletePlayer_Failure() {
        // Arrange
        doThrow(RuntimeException.class).when(dynamoDbClient).deleteItem(any(DeleteItemRequest.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> playerService.deletePlayer(PLAYER_ID));
        verify(dynamoDbClient, times(1)).deleteItem(any(DeleteItemRequest.class));
    }
}
