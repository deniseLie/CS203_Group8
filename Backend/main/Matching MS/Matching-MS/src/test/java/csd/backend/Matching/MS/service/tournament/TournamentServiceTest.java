package csd.backend.Matching.MS.service.tournament;

import csd.backend.Matching.MS.service.sqs.SqsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TournamentServiceTest {

    @Mock
    private DynamoDbClient dynamoDbClient;

    @Mock
    private SqsService sqsService;

    @InjectMocks
    private TournamentService tournamentService;

    private List<Map<String, AttributeValue>> mockPlayers;

    private static final Long PLAYER_ID_1 = 1L;
    private static final Long PLAYER_ID_2 = 1L;
    private static final Long CHAMPION_ID_1 = 1L;
    private static final Long CHAMPION_ID_2 = 2L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Prepare mock players
        mockPlayers = new ArrayList<>();
        Map<String, AttributeValue> player1 = new HashMap<>();
        player1.put("playerId", AttributeValue.builder().n(String.valueOf(PLAYER_ID_1)).build());
        player1.put("championId", AttributeValue.builder().n(String.valueOf(CHAMPION_ID_1)).build());
        mockPlayers.add(player1);

        Map<String, AttributeValue> player2 = new HashMap<>();
        player2.put("playerId", AttributeValue.builder().n(String.valueOf(PLAYER_ID_2)).build());
        player2.put("championId", AttributeValue.builder().n(String.valueOf(CHAMPION_ID_2)).build());
        mockPlayers.add(player2);
    }

    @Test
    void testCreateTournament_Success() {
        // Arrange
        PutItemResponse mockPutResponse = PutItemResponse.builder().build();
        when(dynamoDbClient.putItem(any(PutItemRequest.class))).thenReturn(mockPutResponse);
        doNothing().when(sqsService).sendMessageToQueue(anyString(), anyString(), anyMap());

        // Act
        assertDoesNotThrow(() -> tournamentService.createTournament(mockPlayers));

        // Assert
        verify(dynamoDbClient, times(1)).putItem(any(PutItemRequest.class));
        verify(sqsService, times(1)).sendMessageToQueue(eq("admin"), anyString(), anyMap());
    }


    @Test
    void testSaveMatchToDatabase_Success() {
        // Arrange
        PutItemResponse mockPutResponse = PutItemResponse.builder().build();
        when(dynamoDbClient.putItem(any(PutItemRequest.class))).thenReturn(mockPutResponse);

        // Execute the method indirectly via createTournament
        assertDoesNotThrow(() -> tournamentService.createTournament(mockPlayers));

        // Assert
        verify(dynamoDbClient, times(1)).putItem(any(PutItemRequest.class));
    }


    @Test
    void testTriggerMatchmaking_Success() throws Exception {
        // Mock match item
        Map<String, AttributeValue> matchItem = new HashMap<>();
        matchItem.put("matchId", AttributeValue.builder().n("12345").build());
        matchItem.put("tournamentStart", AttributeValue.builder().s("2023-12-31T12:00:00").build());
        List<AttributeValue> players = new ArrayList<>();
        players.add(AttributeValue.builder().m(Map.of(
                "playerId", AttributeValue.builder().n(String.valueOf(PLAYER_ID_1)).build(),
                "championId", AttributeValue.builder().n(String.valueOf(CHAMPION_ID_1)).build()
        )).build());
        matchItem.put("players", AttributeValue.builder().l(players).build());

        // Mock SQS interaction
        doNothing().when(sqsService).sendMessageToQueue(anyString(), anyString(), anyMap());

        // Execute the method
        assertDoesNotThrow(() -> tournamentService.triggerMatchmaking(matchItem));

        // Verify SQS call
        verify(sqsService, times(1)).sendMessageToQueue(eq("admin"), anyString(), anyMap());
    }

    @Test
    void testPrepareMatchItem_Success() {
        // Execute the method indirectly via createTournament
        assertDoesNotThrow(() -> tournamentService.createTournament(mockPlayers));
    }
}
