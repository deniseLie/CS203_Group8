package csd.backend.Penalty.MS.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PenaltyServiceTest {

    @Mock
    private SqsService sqsService;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PenaltyService penaltyService;

    private static final Long PLAYER_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test: Ban Player - Success
     */
    @Test
    void testBanPlayer_Success() {
        // Arrange
        when(playerService.getBanCount(PLAYER_ID)).thenReturn(2);
        doNothing().when(playerService).updatePlayerStatus(PLAYER_ID, "banned");
        doNothing().when(playerService).updateBanUntil(eq(PLAYER_ID), anyLong());
        doNothing().when(playerService).updateBanCount(eq(PLAYER_ID), eq(3));
        doNothing().when(sqsService).sendMessageToQueue(eq("matchmaking"), anyString(), anyMap());

        // Act
        ResponseEntity<Map<String, Object>> response = penaltyService.banPlayer(PLAYER_ID);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Player banned successfully.", response.getBody().get("message"));

        verify(playerService).getBanCount(PLAYER_ID);
        verify(playerService).updatePlayerStatus(PLAYER_ID, "banned");
        verify(playerService).updateBanUntil(eq(PLAYER_ID), anyLong());
        verify(playerService).updateBanCount(eq(PLAYER_ID), eq(3));
        verify(sqsService).sendMessageToQueue(eq("matchmaking"), anyString(), anyMap());
    }

    /**
     * Test: Ban Player - Failure
     */
    @Test
    void testBanPlayer_Failure() {
        // Arrange
        when(playerService.getBanCount(PLAYER_ID)).thenThrow(new RuntimeException("Test exception"));

        // Act
        ResponseEntity<Map<String, Object>> response = penaltyService.banPlayer(PLAYER_ID);

        // Assert
        assertEquals(500, response.getStatusCode().value());
        assertEquals("Error banning player.", response.getBody().get("message"));

        verify(playerService).getBanCount(PLAYER_ID);
        verifyNoInteractions(sqsService);
    }

    /**
     * Test: Check Player Ban Status - Banned
     */
    @Test
    void testCheckPlayerBanStatus_Banned() {
        // Arrange
        Map<String, AttributeValue> mockPlayer = Map.of(
                "playerId", AttributeValue.builder().n(String.valueOf(PLAYER_ID)).build(),
                "queueStatus", AttributeValue.builder().s("banned").build(),
                "banUntil", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis() + 10000)).build()
        );
        when(playerService.getPlayerDetails(PLAYER_ID)).thenReturn(mockPlayer);

        // Act
        ResponseEntity<Map<String, Object>> response = penaltyService.checkPlayerBanStatus(PLAYER_ID);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("banned", response.getBody().get("queueStatus"));
        assertTrue((long) response.getBody().get("remainingTime") > 0);

        verify(playerService).getPlayerDetails(PLAYER_ID);
    }

    /**
     * Test: Check Player Ban Status - Not Banned
     */
    @Test
    void testCheckPlayerBanStatus_NotBanned() {
        // Arrange
        Map<String, AttributeValue> mockPlayer = Map.of(
                "playerId", AttributeValue.builder().n(String.valueOf(PLAYER_ID)).build(),
                "queueStatus", AttributeValue.builder().s("available").build(),
                "banUntil", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis() - 10000)).build()
        );
        when(playerService.getPlayerDetails(PLAYER_ID)).thenReturn(mockPlayer);

        // Act
        ResponseEntity<Map<String, Object>> response = penaltyService.checkPlayerBanStatus(PLAYER_ID);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("available", response.getBody().get("queueStatus"));
        assertEquals(0L, response.getBody().get("remainingTime"));

        verify(playerService).getPlayerDetails(PLAYER_ID);
    }

    /**
     * Test: Unban Expired Players - Success
     */
    @Test
    void testUnbanExpiredPlayers_Success() {
        // Arrange
        List<Map<String, AttributeValue>> mockBannedPlayers = List.of(
                Map.of("playerId", AttributeValue.builder().n(String.valueOf(1)).build(),
                        "banUntil", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis() - 10000)).build()),
                Map.of("playerId", AttributeValue.builder().n(String.valueOf(2)).build(),
                        "banUntil", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis() + 10000)).build())
        );
        when(playerService.getBannedPlayers()).thenReturn(mockBannedPlayers);
        doNothing().when(playerService).updatePlayerStatus(1L, "available");

        // Act
        assertDoesNotThrow(() -> penaltyService.unbanExpiredPlayers());

        // Assert
        verify(playerService).getBannedPlayers();
        verify(playerService).updatePlayerStatus(1L, "available");
        verify(playerService, never()).updatePlayerStatus(2L, "available");
    }

    /**
     * Test: Add Player to Pool
     */
    @Test
    void testAddPlayerToPool() {
        // Arrange
        doNothing().when(playerService).addPlayerToPool(PLAYER_ID, "available");

        // Act
        assertDoesNotThrow(() -> penaltyService.addPlayerToPool(PLAYER_ID, "available"));

        // Assert
        verify(playerService).addPlayerToPool(PLAYER_ID, "available");
    }

    /**
     * Test: Delete Player
     */
    @Test
    void testDeletePlayer() {
        // Arrange
        doNothing().when(playerService).deletePlayer(PLAYER_ID);

        // Act
        assertDoesNotThrow(() -> penaltyService.deletePlayer(PLAYER_ID));

        // Assert
        verify(playerService).deletePlayer(PLAYER_ID);
    }
}
