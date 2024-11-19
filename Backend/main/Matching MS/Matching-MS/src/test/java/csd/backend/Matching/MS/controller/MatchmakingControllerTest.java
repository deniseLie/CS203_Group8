package csd.backend.Matching.MS.controller;

import csd.backend.Matching.MS.model.request.MatchmakingRequest;
import csd.backend.Matching.MS.model.request.PlayerIdRequest;
import csd.backend.Matching.MS.service.matchmaking.MatchmakingService;
import csd.backend.Matching.MS.service.player.PlayerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class MatchmakingControllerTest {

    @Mock
    private MatchmakingService matchmakingService;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private MatchmakingController matchmakingController;

    private static final Long PLAYER_ID = 1L;
    private static final Long CHAMPION_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test: Successful Join Matchmaking
     * - Add MAX_PLAYERS - 1 players to the "queue" in the database.
     * - Ensure the matchmaking starts successfully.
    */
    @Test
    void testJoinMatchmaking_Success() {
        // Arrange
        MatchmakingRequest request = new MatchmakingRequest();
        request.setPlayerId(PLAYER_ID);
        request.setChampionId(CHAMPION_ID);
        request.setIsSpeedUp(true);

        // Mock MAX_PLAYERS - 1 players in the database
        int MAX_PLAYERS = 10; // Example value
        createMockPlayers(MAX_PLAYERS - 1);

        ResponseEntity<Map<String, Object>> mockedResponse = ResponseEntity.ok(Map.of("message", "Matchmaking started"));
        when(matchmakingService.joinMatchmaking(PLAYER_ID, CHAMPION_ID, true)).thenReturn(mockedResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = matchmakingController.joinMatchmaking(request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Matchmaking started", response.getBody().get("message"));
        verify(matchmakingService, times(1)).joinMatchmaking(PLAYER_ID, CHAMPION_ID, true);
    }

    /**
     * Test: Invalid Input - playerId is not Long
     */
    @Test
    void testJoinMatchmaking_InvalidPlayerId() {
        // Arrange
        MatchmakingRequest request = new MatchmakingRequest();
        request.setPlayerId(null); // Invalid input
        request.setChampionId(123L);

        // Act & Assert
        try {
            matchmakingController.joinMatchmaking(request);
        } catch (Exception e) {
            assertEquals(javax.validation.ConstraintViolationException.class, e.getClass());
        }

        verify(matchmakingService, times(0)).joinMatchmaking(anyLong(), anyLong(), anyBoolean());
    }

    /**
     * Test: Timeout due to insufficient players in the queue
    */
    @Test
    void testJoinMatchmaking_Timeout() {
        // Arrange
        MatchmakingRequest request = new MatchmakingRequest();
        request.setPlayerId(1L);
        request.setChampionId(123L);
        request.setIsSpeedUp(false);

        ResponseEntity<Map<String, Object>> mockedResponse = ResponseEntity.status(408)
                .body(Map.of("message", "Timeout: Unable to find enough players to start a match."));
        when(matchmakingService.joinMatchmaking(1L, 123L, false)).thenReturn(mockedResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = matchmakingController.joinMatchmaking(request);

        // Assert
        assertEquals(408, response.getStatusCode().value());
        assertEquals("Timeout: Unable to find enough players to start a match.", response.getBody().get("message"));
        verify(matchmakingService, times(1)).joinMatchmaking(1L, 123L, false);
    }

    /**
     * Test: Successful Unqueue Player
    */
    @Test
    void testUnqueuePlayer_Success() {
        // Arrange
        PlayerIdRequest request = new PlayerIdRequest();
        request.setPlayerId(PLAYER_ID);

        ResponseEntity<Map<String, Object>> mockedResponse = ResponseEntity.ok(Map.of("message", "Player unqueued successfully"));
        when(matchmakingService.unqueuePlayerFromQueue(PLAYER_ID, "normal")).thenReturn(mockedResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = matchmakingController.unqueuePlayer(request, "normal");

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Player unqueued successfully", response.getBody().get("message"));
        verify(matchmakingService, times(1)).unqueuePlayerFromQueue(PLAYER_ID, "normal");
    }

    /**
     * Joing Matchmaking invalid input (no playerId)
     */
    @Test
    void testJoinMatchmaking_ValidationError() {
        // Arrange
        MatchmakingRequest request = new MatchmakingRequest();
        request.setPlayerId(null); // Invalid input

        // Act & Assert
        try {
            matchmakingController.joinMatchmaking(request);
        } catch (Exception e) {
            assertEquals(javax.validation.ConstraintViolationException.class, e.getClass());
        }

        verify(matchmakingService, times(0)).joinMatchmaking(anyLong(), anyLong(), anyBoolean());
    }

    /**
     * Joing Matchmaking invalid input (queue type)
     */
    @Test
    void testUnqueuePlayer_InvalidQueueType() {
        // Arrange
        PlayerIdRequest request = new PlayerIdRequest();
        request.setPlayerId(PLAYER_ID);

        ResponseEntity<Map<String, Object>> mockedResponse = ResponseEntity.badRequest().body(Map.of("error", "Invalid queue type"));
        when(matchmakingService.unqueuePlayerFromQueue(PLAYER_ID, "invalidQueueType")).thenReturn(mockedResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = matchmakingController.unqueuePlayer(request, "invalidQueueType");

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid queue type", response.getBody().get("error"));
        verify(matchmakingService, times(1)).unqueuePlayerFromQueue(PLAYER_ID, "invalidQueueType");
    }

    /**
     * Utility: Create Mock Players
    */
    private static java.util.List<Map<String, Object>> createMockPlayers(int count) {
        List<Map<String, Object>> players = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            players.add(Map.of(
                    "playerId", (long) i,
                    "queueStatus", "queue",
                    "championId", 123L
            ));
        }
        return players;
    }
    
}
