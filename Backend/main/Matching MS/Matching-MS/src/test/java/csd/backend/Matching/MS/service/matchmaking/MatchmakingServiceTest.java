package csd.backend.Matching.MS.service.matchmaking;

import csd.backend.Matching.MS.model.PlayerStatus;
import csd.backend.Matching.MS.service.player.PlayerService;
import csd.backend.Matching.MS.service.tournament.TournamentService;
import csd.backend.Matching.MS.utils.ResponseUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class MatchmakingServiceTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private TournamentService tournamentService;

    @InjectMocks
    private MatchmakingService matchmakingService;

    private static final Long PLAYER_ID = 1L;
    private static final Long CHAMPION_ID = 1L;
    private static final Long RANK_ID = 5L;
    private static final String QUEUE_STATUS = PlayerStatus.QUEUE.getStatus();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*
     * Test: Join Matchmaking Success
     */
    @Test
    void testJoinMatchmaking_Success() {
        // Arrange
        int MAX_PLAYERS = 10; // Example maximum players for a match
        List<Map<String, AttributeValue>> mockPlayers = new ArrayList<>();

        // Prepare 9 mock players in the queue (MAX_PLAYERS - 1)
        for (int i = 1; i < MAX_PLAYERS; i++) {
            Map<String, AttributeValue> player = new HashMap<>();
            player.put("playerId", AttributeValue.builder().n(String.valueOf(i)).build());
            player.put("queueStatus", AttributeValue.builder().s(PlayerStatus.QUEUE.getStatus()).build());
            player.put("championId", AttributeValue.builder().n(String.valueOf(CHAMPION_ID)).build());
            mockPlayers.add(player);
        }

        // Mock PlayerService responses
        when(playerService.getPlayerRankId(PLAYER_ID))
                .thenReturn(RANK_ID); // Player's rank ID
        when(playerService.checkPlayersInQueue(RANK_ID))
                .thenReturn(mockPlayers); // Mock players already in the queue

        doNothing().when(playerService).updatePlayerStatus(PLAYER_ID, PlayerStatus.QUEUE.getStatus());
        doNothing().when(playerService).updatePlayerChampion(PLAYER_ID, CHAMPION_ID);

        // Mock TournamentService behavior to return a matchId
        Long mockMatchId = 100L;
        when(tournamentService.createTournament(anyList())).thenReturn(mockMatchId);

        // Act
        ResponseEntity<Map<String, Object>> response = matchmakingService.joinMatchmaking(PLAYER_ID, CHAMPION_ID, false);

        // Assert
        assertNotNull(response.getBody(), "Response body is null");
        assertEquals(200, response.getStatusCode().value(), "Status code mismatch");
        assertTrue(response.getBody().containsKey("message"), "Response body does not contain 'message' key");

        // Verify interactions
        verify(playerService, times(1)).updatePlayerStatus(PLAYER_ID, PlayerStatus.QUEUE.getStatus());
        verify(playerService, times(1)).updatePlayerChampion(PLAYER_ID, CHAMPION_ID);
        verify(tournamentService, times(1)).createTournament(anyList());
    }

    /*
     * Test : Join Matchmaking - Player is banned
     */
    @Test
    void testJoinMatchmaking_PlayerBanned() {
        // Mock the response from PlayerService indicating the player is banned
        when(playerService.checkAndHandlePlayerBan(PLAYER_ID))
                .thenReturn(ResponseUtil.createForbiddenResponse("You are banned"));

        // Call the method
        ResponseEntity<Map<String, Object>> response = matchmakingService.joinMatchmaking(PLAYER_ID, CHAMPION_ID, false);

        // Verify the response
        assertEquals(403, response.getStatusCode().value());
        assertEquals("You are banned", response.getBody().get("message"));
        verify(playerService, times(1)).checkAndHandlePlayerBan(PLAYER_ID);
    }

    /*
     * Test: Unqueue Player - Success
     */
    @Test
    void testUnqueuePlayer_Success() {
        // Mock the unqueue operation
        doNothing().when(playerService).updatePlayerStatus(PLAYER_ID, PlayerStatus.AVAILABLE.getStatus());
        doNothing().when(playerService).removePlayerChampion(PLAYER_ID);

        // Call the method
        ResponseEntity<Map<String, Object>> response = matchmakingService.unqueuePlayer(PLAYER_ID);

        // Verify the response
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Player successfully removed from queue", response.getBody().get("message"));
        verify(playerService).updatePlayerStatus(PLAYER_ID, PlayerStatus.AVAILABLE.getStatus());
        verify(playerService).removePlayerChampion(PLAYER_ID);
    }

    /*
     * Test: Check for match - enough player
     */
    @Test
    void testCheckForMatch_EnoughPlayers() {
        // Arrange
        int MAX_PLAYERS = 10; // Example maximum players for a match
        List<Map<String, AttributeValue>> mockPlayers = new ArrayList<>();

        for (int i = 1; i <= MAX_PLAYERS; i++) {
            Map<String, AttributeValue> player = new HashMap<>();
            player.put("playerId", AttributeValue.builder().n(String.valueOf(i)).build());
            player.put("queueStatus", AttributeValue.builder().s("queue").build());
            mockPlayers.add(player);
        }

        // Mock PlayerService and TournamentService responses
        when(playerService.checkPlayersInQueue(RANK_ID)).thenReturn(mockPlayers);
        Long mockMatchId = 200L;
        when(tournamentService.createTournament(anyList())).thenReturn(mockMatchId);

        // Act
        Long matchId = matchmakingService.checkForMatch(RANK_ID, false);

        // Assert
        assertNotNull(matchId);
        assertEquals(mockMatchId, matchId);
        verify(tournamentService, times(1)).createTournament(anyList());
    }

    /*
     * Test: Check for match - not enough player
     */
    @Test
    void testCheckForMatch_NotEnoughPlayers() {
        // Arrange
        int MAX_PLAYERS = 10; // Example maximum players for a match
        List<Map<String, AttributeValue>> mockPlayers = new ArrayList<>();

        // Mock PlayerService response
        when(playerService.checkPlayersInQueue(RANK_ID)).thenReturn(mockPlayers);

        // Act
        Long matchId = matchmakingService.checkForMatch(RANK_ID, false);

        // Assert
        assertNull(matchId); // Not enough players for a match
        verify(tournamentService, never()).createTournament(anyList());
    }

    @Test
    void testUnqueueFromSpeedUpQueue_Success() {
        // Mock the operation
        doNothing().when(playerService).updatePlayerStatus(PLAYER_ID, PlayerStatus.QUEUE.getStatus());

        // Call the method
        ResponseEntity<Map<String, Object>> response = matchmakingService.unqueueFromSpeedUpQueue(PLAYER_ID);

        // Verify the response
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Player successfully removed from queue", response.getBody().get("message"));
        verify(playerService).updatePlayerStatus(PLAYER_ID, PlayerStatus.QUEUE.getStatus());
    }

    /**
     * Utility: Create Mock Players
    */
    private static List<Map<String, AttributeValue>> createMockPlayers(int count) {
        List<Map<String, AttributeValue>> players = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            players.add(Map.of(
                    "playerId", AttributeValue.builder().n(String.valueOf(i)).build(),
                    "queueStatus", AttributeValue.builder().s("queue").build(),
                    "championId", AttributeValue.builder().n(String.valueOf(123L)).build()
            ));
        }
        return players;
    }    
}
